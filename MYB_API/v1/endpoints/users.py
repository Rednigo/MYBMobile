from sqlalchemy.orm import Session
from core.dependencies.dependencies import get_db
from core.schemas.user_schemas import UserCreateSchema, UserLoginSchema, UserSchema
from core.repositories.user_repository import db_get_user_by_username
from core.security.password_check import verify_password
from fastapi import APIRouter, Depends, HTTPException
from core.services.user_service import create_user

router = APIRouter()


# TODO: redo
@router.post("/login", response_model=UserSchema)
def get_users_endpoint(form_data: UserLoginSchema, db: Session = Depends(get_db)):
    user = db_get_user_by_username(db, username=form_data.username)
    if not user:
        raise HTTPException(status_code=400, detail="Incorrect username or password")
    if not verify_password(form_data.password, user.hash_password):
        raise HTTPException(status_code=400, detail="Incorrect username or password")

    return user


@router.post("/register", response_model=UserSchema)
def register(user: UserCreateSchema, db: Session = Depends(get_db)):
    try:
        new_user = create_user(db=db, user_data=user)
        return new_user
    except ValueError as e:
        raise HTTPException(status_code=400, detail=str(e))