from typing import Any, Tuple, Union
from requests import get
from requests.exceptions import RequestException
from pymongo import MongoClient
from os import getenv, path
from json import dump, load

QUACS_URL = (
    "https://raw.githubusercontent.com/quacs/quacs-data/refs/heads/master/semester_data"
)

# ---------------------------------------------------------------------------- #

mongo_server_addr = getenv("MONGO_SERVER_ADDR")
print(mongo_server_addr)

client = MongoClient(mongo_server_addr)
db = client["data"]
majors = db["majors"]
courses = db["courses"]
sections = db["sections"]
semesters = db["semesters"]

# ---------------------------------------------------------------------------- #


def download_data(url: str, filename: str) -> Union[dict, list, None]:
    """
    Download JSON data from the URL, save it to the given file path, and return
    the JSON data.
    """

    try:
        response = get(url)
    except RequestException as e:
        print(f"An error occurred while downloading the JSON data: {e}.")
        return None

    if response.status_code != 200:
        print(f"Failed to download file. HTTP status code: {response.status_code}.")
        return None

    data = response.json()

    with open(filename, "w") as file:
        dump(data, file, indent=4)

    print(f"File downloaded and JSON data saved to {filename}.")
    return data


def get_data(url: str, filename: str) -> Union[dict, list, None]:
    """
    Check if the file exists. If it does, return the existing JSON data.
    Otherwise, download JSON from the URL.
    """

    if not path.exists(filename):
        print(f"File '{filename}' does not exist. Downloading JSON data...")
        return download_data(url, filename)

    print(f"File '{filename}' already exists.")
    with open(filename, "r") as file:
        data = load(file)
        return data


# ---------------------------------------------------------------------------- #


def get_courses(semester):
    return get_data(
        f"{QUACS_URL}/{semester}/courses.json", f"data/courses-{semester}.json"
    )


def get_schools(semester):
    return get_data(
        f"{QUACS_URL}/{semester}/schools.json", f"data/schools-{semester}.json"
    )


def get_semester_from_code(semester: str) -> Union[None, Tuple[int, str]]:
    year, season = semester[0:4], semester[4:]

    match season:
        case "01":
            return int(year), "spring"
        case "06":
            return int(year), "summer"
        case "09":
            return int(year), "fall"
        case _:
            return None


# ---------------------------------------------------------------------------- #


def push_semester(data: dict) -> Any:
    result = semesters.update_one(data, {"$set": data}, upsert=True)

    print(f"Semester: '{data['season']}' / {data['year']}.")

    if result.upserted_id:
        return semesters.find_one({ '_id': result.upserted_id })

    return semesters.find_one(data)


def push_major(data: dict) -> Any:
    result = majors.update_one({"code": data["code"]}, {"$set": data}, upsert=True)

    print(f"Major '{data['code']}'.")

    if result.upserted_id:
        return majors.find_one({ '_id': result.upserted_id })

    return majors.find_one({"code": data["code"]})


def push_course(data: dict) -> Any:
    result = courses.update_one({"code": data["code"]}, {"$set": data}, upsert=True)

    print(f"Course '{data['code']}'.")

    if result.upserted_id:
        return courses.find_one({ '_id': result.upserted_id })

    return courses.find_one({"code": data["code"]})


def push_section(data: dict) -> Any:
    result = sections.update_one(
        {"number": data["number"], "course": data["course"]},
        {"$set": data},
        upsert=True,
    )

    print(f"Section {data['number']} for {data["course"]}.")

    if result.upserted_id:
        return sections.find_one({ '_id': result.upserted_id })

    return sections.find_one({"number": data["number"], "course": data["course"]})


# ---------------------------------------------------------------------------- #


def push_data(semester_code):
    semester_data = get_semester_from_code(semester_code)
    if semester_data is None:
        print(f"Semester name {semester_data} for {semester_code} does not exist.")
        return

    year, season = semester_data
    semester = push_semester({"year": year, "season": season})

    # ------------------------------------------------------------------------ #

    schools = get_schools(semester_code)
    if schools is None:
        print(f"Couldn't get school data for {semester_code}.")
        return

    for school in schools:
        for major in school["depts"]:
            push_major(
                {
                    "code": major["code"],
                    "school": school["name"],
                    "name": major["name"],
                    "semester": semester["_id"],
                }
            )

    # ------------------------------------------------------------------------ #

    course_data = get_courses(semester_code)
    if course_data is None:
        print(f"Couldn't get course data for {semester_code}.")
        return

    for major_data in course_data:
        major = majors.find_one({"code": major_data["code"]})
        if major is None:
            print(f"Major {major_data["code"]} not found.")
            continue

        for course_data in major_data["courses"]:
            course = push_course(
                {
                    "major": major["_id"],
                    "semester": semester["_id"],
                    "code": course_data["id"],
                    "name": course_data["title"],
                }
            )

            for section in course_data["sections"]:

                profs = [
                    slot["instructor"].split(", ") for slot in section["timeslots"]
                ]

                push_section(
                    {
                        "course": course["_id"],
                        "professors": profs,
                        "number": section["sec"],
                    }
                )


# ---------------------------------------------------------------------------- #


data = push_data("202409")
