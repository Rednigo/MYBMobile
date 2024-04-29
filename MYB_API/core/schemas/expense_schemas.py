from datetime import datetime
from pydantic import BaseModel


class ExpenseBase(BaseModel):
    expense_name: str
    amount: float
    date: datetime
    category_id: int


class ExpenseUpdateSchema(BaseModel):
    expense_name: str | None = None
    amount: float | None = None


class ExpenseCreateSchema(ExpenseBase):
    pass


class ExpenseSchema(ExpenseBase):
    id: int
