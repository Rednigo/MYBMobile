from sqlalchemy.orm import Session

from core.models.models import User
from core.repositories.user_repository import db_create_user, db_get_user_by_username, db_get_user, db_get_users
from core.schemas.user_schemas import UserCreateSchema
from core.security.password_check import get_password_hash


def create_user(db: Session, user_data: UserCreateSchema):
    # Check if the username already exists
    existing_user = db_get_user_by_username(db, username=user_data.username)
    if existing_user:
        raise ValueError("Username already registered")

    # Hash the password
    hashed_password = get_password_hash(user_data.password)

    # Create a new User model instance
    new_user = User(
        username=user_data.username,
        email=user_data.email,
        hashed_password=hashed_password,
    )

    # Save the new user to the database
    return db_create_user(db, new_user)


def get_user(db: Session, user_id: int):
    user = db_get_user(db, user_id)
    if not user:
        return None
    return user


def get_all_users(db: Session):

    users = db_get_users(db)
    if not users:
        return None
    return users
