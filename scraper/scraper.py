# -*- coding: utf-8 -*-
"""
Created on Tue Sep 24 13:22:27 2024

@author: Adam
"""
import json
import requests

from pymongo import MongoClient
from dotenv import load_dotenv
import os

load_dotenv("../config.env")
mongo_server_addr = os.getenv("MONGO_SERVER_ADDR")
print(mongo_server_addr)

# Connect to MongoDB (replace the URL with your connection string if using Atlas)
client = MongoClient(mongo_server_addr)  # Use your MongoDB URL here
db = client["data"]  # The name of your database
majors_col = db["Majors"]  # The collection for majors
classes_col = db["Classes"]  # The collection for classes
sections_col = db["Sections"]  # The collection for sections
semesters_col = db["Semesters"]

def get_courses_json(sem):
    url = 'https://raw.githubusercontent.com/quacs/quacs-data/refs/heads/master/semester_data/{}/courses.json'.format(sem)
    data = requests.get(url)
    return data.json()

def get_schools_json(sem):
    url = 'https://raw.githubusercontent.com/quacs/quacs-data/refs/heads/master/semester_data/{}/schools.json'.format(sem)
    data = requests.get(url)
    return data.json()

def map_sem_code(sem):
    year = sem[0:4]
    season = sem[4:]
    if season == '09':
        return year, "fall"
    if season == '01':
        return year, "spring"
    if season == '06':
        return year, "summer"

def update_db(sem):
    
    # Major table:
    # { 
    #   "id": "String",
    #   "department": "String",
    #   "code": "String",
    #   "name": "String",
    # }
    
    # Class table:
    # {
    #   "id": "String",
    #   "major": "ref<Major>",
    #   "semester": "ref<Semester>",
    #   "code": "String",
    # }
    # define id as 
    
    # Section table:
    # {
    #   "id": "String",
    #   "class": "ref<Class>",
    #   "professors": "List<String>",
    #   "section": "Number",
    # }
    # define id as classid+secnum

    courses = get_courses_json(sem)
    schools = get_schools_json(sem)
    
    semname = map_sem_code(sem)
    semid = "{}-{}".format(semname[0], semname[1])
    
    semester_object = semesters_col.find_one({"id": semid})
    if semester_object is None:
        # If the semid is not found, insert it
        semester_data = {
            "id": semid,
            "type": semname[1],
            "year": semname[0],
        }
        result = semesters_col.update_one({"id": semid}, {"$set": semester_data}, upsert=True)
        print(f"Inserted new semester {semid}")
        
        if result.upserted_id:
            sem_object_id = result.upserted_id
            print(f"Inserted new semester {semid} with _id {sem_object_id}")
        else:
            # If no new document was inserted, find the document by semid to get its _id
            sem_object_id = semesters_col.find_one({"id": semid})["_id"]
            print(f"Found existing semester {semid} with _id {sem_object_id}")
        
    else:
        # If the semester is found, get the _id
        sem_object_id = semester_object["_id"]
        print(f"Semester {semid} already exists with _id {sem_object_id}")
    
    for school in schools:
        
        schoolname = school['name']
        schoolname = schoolname+"TEST"
        #print(schoolname)
        
        for dept in school['depts']:
    
            deptcode = dept['code']
            major_id = deptcode
            deptname = dept['name']
    
            major_data = {
                "id": major_id,
                "department": schoolname,
                "code": deptcode,
                "name": deptname,
            }
            majors_col.update_one({"id": major_id}, {"$set": major_data}, upsert=True)
    
    for code in courses:
        
        deptname = code['name']
        deptcode = code['code']
        major_id = deptcode
        
        # Find the Major document and get its ObjectId
        major_object = majors_col.find_one({"id": major_id})
        if major_object is None:
            print(f"Major {major_id} not found for department {deptname}")
            continue  # Skip if no corresponding major is found
    
        major_object_id = major_object['_id']  # This is the ObjectId reference
        
        for course in code['courses']:
            
            courseid = course['id'] #eg "CSCI-1200"
            coursetitle = course['title']
            
            class_data = {
                "id": "{}-{}".format(courseid, sem),
                "major": major_object_id,
                "semester": sem_object_id,
                "code": courseid,
                "name": coursetitle,
            }
            result = classes_col.update_one({"id": courseid}, {"$set": class_data}, upsert=True)
            
            # Retrieve the _id of the class (for existing or newly inserted doc)
            if result.upserted_id:
                class_id = result.upserted_id  # This will give the new _id if inserted
            else:
                # If no new insert, find the existing class by its courseid
                existing_class = classes_col.find_one({"id": "{}-{}".format(courseid, sem)})
                if existing_class:
                    class_id = existing_class["_id"]
                else:
                    print(f"Class with id {courseid}-{sem} not found in Classes collection")
                    continue  # Skip further processing if no class is found
            
            #print(courseid, coursetitle)
            
            for section in course['sections']:
                secnum = section['sec']
                #print("\t"+secnum)
                
                profs = []
                for slot in section['timeslots']:
                    profs.extend(slot['instructor'].split(", "))
                #print("\t", profs)
                
                section_id = f"{courseid}-{secnum}"
                section_data = {
                    "id": section_id,
                    "class": class_id,
                    "professors": profs,
                    "section": secnum,
                }
                sections_col.update_one({"id": section_id}, {"$set": section_data}, upsert=True)
    
    
    
data = update_db("202409")
    