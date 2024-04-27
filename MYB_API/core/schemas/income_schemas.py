from pydantic import BaseModel


class IncomeBase(BaseModel):
    name: str
    amount: float
    user_id: int


class IncomeUpdateSchema(BaseModel):
    name: str | None = None
    amount: float | None = None
    user_id: int | None = None


class IncomeCreateSchema(IncomeBase):
    pass


class IncomeSchema(IncomeBase):
    id: int
