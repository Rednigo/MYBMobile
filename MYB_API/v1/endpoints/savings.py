from typing import List
from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from core.dependencies.dependencies import get_db
from core.services.savings_service import (
    create_savings,
    get_savings_by_user_id,
    get_savings_by_id,
    update_savings,
    delete_savings
)
from core.schemas.savings_schemas import SavingsCreateSchema, SavingsSchema, SavingsUpdateSchema


router = APIRouter()


@router.post("/savings", response_model=SavingsSchema)
def create_savings_endpoint(savings: SavingsCreateSchema, db: Session = Depends(get_db)):
    return create_savings(db=db, savings=savings)


@router.get("/savings", response_model=List[SavingsSchema])
def get_savings_endpoint(user_id: int, db: Session = Depends(get_db)):
    return get_savings_by_user_id(db=db, user_id=user_id)


@router.get("/savings/{savings_id}", response_model=SavingsSchema)
def get_savings_by_id_endpoint(savings_id: int, db: Session = Depends(get_db)):
    savings = get_savings_by_id(db=db, savings_id=savings_id)
    if savings is None:
        raise HTTPException(status_code=404, detail="Savings not found")
    return savings


@router.put("/savings/{savings_id}", response_model=SavingsSchema)
def update_savings_endpoint(savings_id: int, savings: SavingsUpdateSchema, db: Session = Depends(get_db)):
    return update_savings(db=db, savings_id=savings_id, savings=savings)


@router.delete("/savings/{savings_id}", response_model=SavingsSchema)
def delete_savings_endpoint(savings_id: int, db: Session = Depends(get_db)):
    return delete_savings(db=db, savings_id=savings_id)
