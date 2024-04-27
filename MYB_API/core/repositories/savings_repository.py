from sqlalchemy.orm import Session
from core.models.models import Savings
from core.schemas.savings_schemas import SavingsCreateSchema, SavingsUpdateSchema


def db_get_savings(db: Session, savings_id: int):
    return db.query(Savings).filter(Savings.id == savings_id).first()


def db_get_savings_by_user_id(db: Session, user_id: int, skip: int = 0, limit: int = 100):
    return db.query(Savings).filter(Savings.user_id == user_id).offset(skip).limit(limit).all()


def db_create_savings(db: Session, savings: SavingsCreateSchema, user_id: int):
    db_savings = Savings(**savings.dict(), user_id=user_id)
    db.add(db_savings)
    db.commit()
    db.refresh(db_savings)
    return db_savings


def db_update_savings(db: Session, savings_id: int, savings: SavingsUpdateSchema):
    db_savings = db_get_savings(db, savings_id)
    if db_savings:
        update_data = savings.dict(exclude_unset=True)
        for key, value in update_data.items():
            setattr(db_savings, key, value)
        db.commit()
        db.refresh(db_savings)
    return db_savings


def db_delete_savings(db: Session, savings_id: int):
    db_savings = db_get_savings(db, savings_id)
    if db_savings:
        db.delete(db_savings)
        db.commit()
    return db_savings
