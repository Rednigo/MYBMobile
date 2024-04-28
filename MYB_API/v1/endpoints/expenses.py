from typing import List
from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from core.dependencies.dependencies import get_db
from core.services.expense_service import (
    create_expense,
    get_expenses_by_category_id,
    get_expense_by_id,
    update_expense,
    delete_expense
)
from core.schemas.expense_schemas import ExpenseCreateSchema, ExpenseSchema, ExpenseUpdateSchema


router = APIRouter()


@router.post("/expenses", response_model=ExpenseSchema)
def create_expense_endpoint(expense: ExpenseCreateSchema, db: Session = Depends(get_db)):
    return create_expense(db=db, expense=expense)


@router.get("/expenses", response_model=List[ExpenseSchema])
def get_expenses_endpoint(category_id: int, db: Session = Depends(get_db)):
    return get_expenses_by_category_id(db=db, category_id=category_id)


@router.get("/expenses/{expense_id}", response_model=ExpenseSchema)
def get_expense_by_id_endpoint(expense_id: int, db: Session = Depends(get_db)):
    expense = get_expense_by_id(db=db, expense_id=expense_id)
    if expense is None:
        raise HTTPException(status_code=404, detail="Expense not found")
    return expense


@router.put("/expenses/{expense_id}", response_model=ExpenseSchema)
def update_expense_endpoint(expense_id: int, expense: ExpenseUpdateSchema, db: Session = Depends(get_db)):
    return update_expense(db=db, expense_id=expense_id, expense=expense)


@router.delete("/expenses/{expense_id}", response_model=ExpenseSchema)
def delete_expense_endpoint(expense_id: int, db: Session = Depends(get_db)):
    return delete_expense(db=db, expense_id=expense_id)
