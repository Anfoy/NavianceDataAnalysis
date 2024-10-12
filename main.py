import requests
import csv
import os

# The initial request URL to get the list of colleges
url = "https://blue-ridge-api.naviance.com/college/scattergram"

# Your JWT token (place holder for actual token)
jwt_token = "Enter auth token here"

# Set up the headers with the JWT for authentication
headers = {
    "Authorization": f"Bearer {jwt_token}"
}

# Function to write data to CSV
def write_to_csv(filename, fieldnames, data_list):
    try:
        with open(filename, mode='w', newline='') as file:
            writer = csv.DictWriter(file, fieldnames=fieldnames)
            writer.writeheader()
            for data in data_list:
                writer.writerow(data)
        print(f"CSV {filename} has been written successfully.")
    except IOError as e:
        print(f"An error occurred while writing to {filename}: {str(e)}")

# Function to extract and write UUID, name, and ID to CSV
def extract_and_write_college_info():
    response = requests.get(url, headers=headers)

    if response.status_code == 200:
        data = response.json()
        college_info_list = []

        # Extract UUID, school name, and school ID
        for college in data:
            if 'coreMapping' in college and 'uuid' in college['coreMapping']:
                uuid_value = college['coreMapping']['uuid']
                school_name = college.get('name', 'N/A')
                school_id = college.get('id', 'N/A')

                # Add the college info to the list
                college_info_list.append({
                    'UUID': uuid_value,
                    'School Name': school_name,
                    'School ID': school_id
                })

        # Write the college info to a CSV file
        write_to_csv('college_info.csv', ['UUID', 'School Name', 'School ID'], college_info_list)

        return [college['UUID'] for college in college_info_list]  # Return list of UUIDs for further processing
    else:
        print(f"Failed to retrieve UUIDs. Status code: {response.status_code}")
        return []

# Method to retrieve application statistics for a given UUID
def get_application_stats(uuid_value, base_url):
    new_url = base_url + uuid_value
    application_stats_data = []

    try:
        uuid_response = requests.get(new_url, headers=headers)
        if uuid_response.status_code == 200:
            uuid_data = uuid_response.json()

            # Extract data from applicationStatistics
            application_stats = uuid_data.get('applicationStatistics', {})
            if application_stats:
                accepted_stats = application_stats.get('accepted', {})

                act_stats = accepted_stats.get('act', {}).get('actComposite', {})
                sat_stats = accepted_stats.get('sat', {}).get('satTotal', {})
                weighted_gpa_stats = accepted_stats.get('weightedGpa', {})

                # Append data to the list
                application_stats_data.append({
                    'UUID': uuid_value,
                    'act_25th': act_stats.get('25'),
                    'act_75th': act_stats.get('75'),
                    'sat_25th': sat_stats.get('25'),
                    'sat_75th': sat_stats.get('75'),
                    'weighted_gpa_25th': weighted_gpa_stats.get('25'),
                    'weighted_gpa_75th': weighted_gpa_stats.get('75')
                })
        else:
            print(f"Failed to retrieve stats data for UUID: {uuid_value}. Status code: {uuid_response.status_code}")

    except Exception as e:
        print(f"An error occurred for UUID: {uuid_value}. Error: {str(e)}")

    return application_stats_data

# Method to retrieve applicant data for a given UUID
def get_applicant_data(uuid_value, base_url):
    new_url = base_url + uuid_value
    applicants_data = []

    try:
        uuid_response = requests.get(new_url, headers=headers)
        if uuid_response.status_code == 200:
            uuid_data = uuid_response.json()

            # Safely check if 'scattergrams' exists before accessing it
            scattergrams = uuid_data.get('scattergrams', None)
            if scattergrams:
                # Safely check if 'weightedGpa' exists before accessing it
                weighted_gpa = scattergrams.get('weightedGpa', None)
                if weighted_gpa:
                    # Process ACT and SAT data
                    for key in ['act', 'sat']:
                        if key in weighted_gpa:
                            for category, app_list in weighted_gpa[key].get('apps', {}).items():
                                for app in app_list:
                                    applicants_data.append({
                                        'UUID': uuid_value,
                                        'type': category,
                                        'gpa': app.get('gpa'),
                                        'actComposite': app.get('actComposite'),
                                        'highestComboSatWWConvertedTo1600': app.get('highestComboSatWWConvertedTo1600', 'N/A'),
                                        'highestComboSat': app.get('highestComboSat'),
                                        'studentSATComposite': app.get('studentSAT1600Composite')
                                    })
        else:
            print(f"Failed to retrieve applicant data for UUID: {uuid_value}. Status code: {uuid_response.status_code}")

    except Exception as e:
        print(f"An error occurred for UUID: {uuid_value}. Error: {str(e)}")

    return applicants_data

# Method to process UUIDs and fetch stats data
def process_and_write_stats_data(uuids):
    base_url = "https://blue-ridge-api.naviance.com/application-statistics/uuid/"
    all_application_stats_data = []
    all_applicants_data = []

    total_uuids = len(uuids)

    for index, uuid_value in enumerate(uuids, start=1):
        print(f"Processing {index}/{total_uuids}: UUID {uuid_value}")

        # Fetch data for this UUID (applicant data and stats)
        stats_data = get_application_stats(uuid_value, base_url)
        user_stats_data = get_applicant_data(uuid_value, base_url)
        all_application_stats_data.extend(stats_data)
        all_applicants_data.extend(user_stats_data)

    # Write the application statistics data to a CSV file
    write_to_csv('application_stats_data.csv', ['UUID', 'act_25th', 'act_75th', 'sat_25th', 'sat_75th', 'weighted_gpa_25th', 'weighted_gpa_75th'], all_application_stats_data)
    write_to_csv('appdata.csv', ['UUID', 'type', 'gpa', 'actComposite', 'highestComboSatWWConvertedTo1600', 'highestComboSat', 'studentSATComposite'], all_applicants_data)
    print("Data for application statistics and applicant data has been written to CSV.")

# Main processing function
def main():
    # Step 1: Extract college info (UUID, name, and ID) and write it to CSV
    uuids = extract_and_write_college_info()

    # Step 2: If UUIDs were found, fetch additional application statistics and write to another CSV
    if uuids:
        process_and_write_stats_data(uuids)

# Call the main function to execute the process
main()
