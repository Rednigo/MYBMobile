from typing import List
from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from core.dependencies.dependencies import get_db
from core.services.income_service import (
    create_income,
    get_incomes_by_user_id,
    get_income_by_id,
    update_income,
    delete_income
)
from core.schemas.income_schemas import IncomeCreateSchema, IncomeSchema, IncomeUpdateSchema

router = APIRouter()


@router.post("/incomes", response_model=IncomeSchema)
def create_income_endpoint(income: IncomeCreateSchema, db: Session = Depends(get_db)):
    return create_income(db=db, income=income, user_id=income.user_id)


@router.get("/incomes", response_model=List[IncomeSchema])
def get_incomes_endpoint(user_id: int, db: Session = Depends(get_db)):
    return get_incomes_by_user_id(db=db, user_id=user_id)


@router.get("/incomes/{income_id}", response_model=IncomeSchema)
def get_income_by_id_endpoint(income_id: int, db: Session = Depends(get_db)):
    income = get_income_by_id(db=db, income_id=income_id)
    if income is None:
        raise HTTPException(status_code=404, detail="Income not found")
    return income


@router.put("/incomes/{income_id}", response_model=IncomeSchema)
def update_income_endpoint(income_id: int, income: IncomeUpdateSchema, db: Session = Depends(get_db)):
    return update_income(db=db, income_id=income_id, income=income)


@router.delete("/incomes/{income_id}", response_model=IncomeSchema)
def delete_income_endpoint(income_id: int, db: Session = Depends(get_db)):
    return delete_income(db=db, income_id=income_id)
