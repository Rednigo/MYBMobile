from sqlalchemy.orm import Session
from core.models.models import User
from core.schemas.user_schemas import UserCreateSchema, UserUpdateSchema


def db_get_user(db: Session, user_id: int):
    return db.query(User).filter(User.id == user_id).first()


def db_get_user_by_email(db: Session, email: str):
    return db.query(User).filter(User.email == email).first()


def db_get_user_by_username(db: Session, username: str):
    return db.query(User).filter(User.username == username).first()


def db_get_users(db: Session, skip: int = 0, limit: int = 100):
    return db.query(User).offset(skip).limit(limit).all()


def db_create_user(db: Session, user: User):
    db.add(user)
    db.commit()
    db.refresh(user)
    return user


def db_update_user(db: Session, user_id: int, user: UserUpdateSchema):
    db_user = db.query(User).filter(User.id == user_id).first()
    for var, value in vars(user).items():
        setattr(db_user, var, value) if value else None
    db.commit()
    return db_user


def db_delete_user(db: Session, user_id: int):
    db_user = db.query(User).filter(User.id == user_id).first()
    db.delete(db_user)
    db.commit()
    return db_user
