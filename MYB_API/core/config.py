from dotenv import load_dotenv  # pip3 install python-dotenv
import os

load_dotenv()

# DB
DB_HOST = os.getenv("DB_HOST", "localhost")  # Providing default values is a good practice
DB_PORT = os.getenv("DB_PORT", "3306")
DB_NAME = os.getenv("DB_NAME", "myb_mobile")
DB_USER = os.getenv("DB_USER", "root")
DB_PASS = os.getenv("DB_PASS", "1q2w3e4r")

# API CONFIGURATION
SERVER_HOST = os.getenv("SERVER_HOST", "192.168.0.163")
SERVER_PORT = int(os.getenv("SERVER_PORT", 8080))
LOG_LEVEL = os.getenv("LOG_LEVEL", "info")
DEBUG_RELOAD = bool(os.getenv("DEBUG_RELOAD", True))

# EMAIL
# EMAIL_ADDRESS = os.getenv("EMAIL_ADDRESS", "default_email@example.com")
# EMAIL_PASSWORD = os.getenv("EMAIL_PASSWORD")
