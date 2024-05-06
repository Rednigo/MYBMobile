from sqlalchemy.orm import Session
from core.dependencies.dependencies import get_db
from core.schemas.user_schemas import UserCreateSchema, UserLoginSchema, UserSchema, UpdateSettings
from core.repositories.user_repository import db_get_user_by_username
from core.security.password_check import verify_password
from fastapi import APIRouter, Depends, HTTPException

from core.services.statistic_service import get_statistic
from core.services.user_service import create_user, update_user_settings, get_user_by_id

router = APIRouter()


# TODO: redo
@router.post("/login", response_model=UserSchema)
def get_users_endpoint(form_data: UserLoginSchema, db: Session = Depends(get_db)):
    user = db_get_user_by_username(db, username=form_data.username)
    if not user:
        raise HTTPException(status_code=400, detail="Incorrect username or password")
    if not verify_password(form_data.password, user.hashed_password):
        raise HTTPException(status_code=400, detail="Incorrect username or password")

    return user


@router.post("/register", response_model=UserSchema)
def register(user: UserCreateSchema, db: Session = Depends(get_db)):
    try:
        new_user = create_user(db=db, user_data=user)
        return new_user
    except ValueError as e:
        raise HTTPException(status_code=400, detail=str(e))


@router.get("/user/{user_id}", response_model=UserSchema)
def get_savings_by_id_endpoint(user_id: int, db: Session = Depends(get_db)):
    user = get_user_by_id(db=db, user_id=user_id)
    if user is None:
        raise HTTPException(status_code=404, detail="User is not found")
    return user


@router.get("/statistic")
def get_statistic_endpoint(user_id: int, db: Session = Depends(get_db)):
    summary = get_statistic(user_id, db)
    return summary


@router.put("/settings", response_model=UserSchema)
def register(user: UpdateSettings, db: Session = Depends(get_db)):
    try:
        new_user = update_user_settings(db=db, user_data=user)
        return new_user
    except ValueError as e:
        raise HTTPException(status_code=400, detail=str(e))
