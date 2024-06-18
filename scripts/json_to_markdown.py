import json
import sys

def json_to_markdown_table(file_path):
    with open(file_path, 'r') as f:
        data = json.load(f)

    if not data:
        return

    headers = list(data.values())[0].keys()
    table = []
    table.append(" | ".join(["Transaction"] + list(headers)))
    table.append(" | ".join("---" for _ in headers))

    for key, row in data.items():
        table.append(" | ".join([key] + [str(row[h]) for h in headers]))

    return "\n".join(table)


if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("Usage: python json_to_markdown.py <file_path>")
        sys.exit(1)
    file_path = sys.argv[1]
    print(json_to_markdown_table(file_path))