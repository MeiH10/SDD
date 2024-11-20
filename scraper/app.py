"""
Scrapes all data from QuACS, and puts it into our database.
"""

from typing import Tuple, Union
from os import getenv, path
from json import dump, load
from bson import DBRef, ObjectId
from requests import get
from requests.exceptions import RequestException
from pymongo import DeleteMany, MongoClient, InsertOne

QUACS_URL = (
    "https://raw.githubusercontent.com/quacs/quacs-data/refs/heads/master/semester_data"
)

# ---------------------------------------------------------------------------- #

mongo_server_addr = getenv("MONGO_SERVER_ADDR")
print(mongo_server_addr)

client = MongoClient(mongo_server_addr)
db = client["data"]

# ---------------------------------------------------------------------------- #


def download_data(url: str, filename: str) -> Union[dict, list, None]:
    """
    Download JSON data from the URL, save it to the given file path, and return
    the JSON data.
    """

    try:
        response = get(url, timeout=10)
    except RequestException as e:
        print(f"An error occurred while downloading the JSON data: {e}.")
        return None

    if response.status_code != 200:
        print(f"Failed to download file. HTTP status code: {response.status_code}.")
        return None

    data = response.json()

    with open(filename, "w", encoding="utf-8") as file:
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
    with open(filename, "r", encoding="utf-8") as file:
        data = load(file)
        return data


# ---------------------------------------------------------------------------- #


def get_courses(semester):
    """
    Get the quacs data for courses.
    """
    return get_data(
        f"{QUACS_URL}/{semester}/courses.json", f"data/courses-{semester}.json"
    )


def get_schools(semester):
    """
    Get the quacs data for schools.
    """
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


def replace_semester(semester_code):
    semester_data = get_semester_from_code(semester_code)
    if semester_data is None:
        print(f"Semester name {semester_data} for {semester_code} does not exist.")
        return

    year, season = semester_data

    semester = {
        "_id": ObjectId(),
        "year": year,
        "season": season
    }

    # ------------------------------------------------------------------------ #

    quacs_school_data = get_schools(semester_code)
    if quacs_school_data is None:
        print(f"Couldn't get school data for {semester_code}.")
        return
    
    schools = []
    majors = []
    
    for school_data in quacs_school_data:
        schools.append(school := {
            "_id": ObjectId(),
            "name": school_data["name"],
            "semester": semester["_id"],
        })
        
        for major in school_data["depts"]:
            majors.append({
                "_id": ObjectId(),
                "code": major["code"],
                "name": major["name"],
                "school": school["_id"],
                "semester": semester["_id"],
            })

    # ------------------------------------------------------------------------ #

    quacs_course_data = get_courses(semester_code)
    if quacs_course_data is None:
        print(f"Couldn't get course data for {semester_code}.")
        return

    courses = []
    sections = []

    for major in majors:
        major_data = next((m for m in quacs_course_data if m["code"] == major["code"]), None)
        if major_data is None:
            print(f"Major {major["code"]} not found.")
            continue

        for course_data in major_data["courses"]:
            courses.append(
                course := {
                    "_id": ObjectId(),
                    "code": course_data["id"],
                    "name": course_data["title"],
                    "semester": semester["_id"],
                    "school": major['school'],
                    "major": major["_id"],
                }
            )

            for section in course_data["sections"]:
                profs = [
                    slot["instructor"].split(", ") for slot in section["timeslots"]
                ]

                sections.append(
                    section := {
                        "_id": ObjectId(),
                        "professors": profs,
                        "number": section["sec"],
                        "semester": semester["_id"],
                        "school": major['school'],
                        "major": major["_id"],
                        "course": course["_id"],
                    }
                )
    
    print(f"Writing semesters: {semester}")
    db["semesters"].bulk_write([DeleteMany({}), InsertOne(semester)])
    print("Semesters written!")

    print(f"Writing schools: {schools}")
    db["schools"].bulk_write([DeleteMany({})] + [InsertOne(s) for s in schools])
    print("Schools written!")

    print(f"Writing majors: {[m['code'] for m in majors]}")
    db["majors"].bulk_write([DeleteMany({})] + [InsertOne(m) for m in majors])
    print("Majors written!")

    print(f"Writing courses: {[c['code'] for c in courses]}")
    db["courses"].bulk_write([DeleteMany({})] + [InsertOne(c) for c in courses])
    print("Courses written!")

    print(f"Writing sections: {[s['number'] for s in sections]}")
    db["sections"].bulk_write([DeleteMany({})] + [InsertOne(s) for s in sections])
    print("Sections written!")


# ---------------------------------------------------------------------------- #


replace_semester("202409")
