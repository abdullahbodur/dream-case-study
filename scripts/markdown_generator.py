import json
import sys
import os
import argparse
import subprocess

project_dir = os.path.join(os.path.dirname(os.path.realpath(__file__)), '..')
reports_path = os.path.join(project_dir, 'deployment', 'performance-test', 'reports')

def json_to_markdown_table(file_path):
    with open(file_path, 'r') as f:
        data = json.load(f)

    if not data:
        return

    headers = list(data.values())[0].keys()
    table = []
    table.append("| " + " | ".join(list(headers)) + " |")
    table.append("| " + " | ".join("---" for _ in list(headers)) + " |")

    for key, row in data.items():
        table.append("| " + " | ".join([str(row[h]) for h in headers]) + " |")

    return "\n".join(table)


def get_all_statistics():
  # for over all folders in reports directory
  for folder in os.listdir(reports_path):
    # for each file in folder
    for file in os.listdir(os.path.join(reports_path, folder)):
      if file.endswith('.json'):
        print(f"#### {folder}")
        print(json_to_markdown_table(os.path.join(reports_path, folder, file)))

get_all_statistics()