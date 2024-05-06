from datetime import datetime
from sqlalchemy import func, extract
from sqlalchemy.orm import Session
from core.models.models import Income, Expense, Savings, User, ExpenseCategory


def get_financial_summary(user_id: int, db: Session):
    # Find the earliest date among income, expense, and savings for the given user
    user = db.query(User).get(user_id)
    if not user:
        return {
            "months": [],
            "incomes": [],
            "expenses": [],
            "saved": []
        }  # Return empty if user does not exist

    earliest_income = db.query(func.min(Income.date)).filter(Income.user_id == user_id).scalar()
    earliest_savings = db.query(func.min(Savings.date)).filter(Savings.user_id == user_id).scalar()

    # Fetch all category IDs for the user
    category_ids = [category.id for category in db.query(ExpenseCategory.id).filter(ExpenseCategory.user_id == user_id).all()]
    earliest_expense = db.query(func.min(Expense.date)).filter(Expense.category_id.in_(category_ids)).scalar()

    start_date = min([d for d in [earliest_income, earliest_expense, earliest_savings] if d is not None])

    financial_data = {
        "months": [],
        "incomes": [],
        "expenses": [],
        "saved": []
    }

    if start_date is None:
        return financial_data  # No data available

    current_date = datetime.utcnow()
    while start_date <= current_date:
        year, month = start_date.year, start_date.month

        # Calculate totals for the month for this user
        monthly_income = db.query(func.sum(Income.amount)).filter(
            Income.user_id == user_id,
            extract('year', Income.date) == year,
            extract('month', Income.date) == month
        ).scalar() or 0

        monthly_expenses = db.query(func.sum(Expense.amount)).filter(
            Expense.category_id.in_(category_ids),
            extract('year', Expense.date) == year,
            extract('month', Expense.date) == month
        ).scalar() or 0

        monthly_savings = db.query(func.sum(Savings.amount)).filter(
            Savings.user_id == user_id,
            extract('year', Savings.date) == year,
            extract('month', Savings.date) == month
        ).scalar() or 0

        financial_data['months'].append(f"{year}-{month:02d}")
        financial_data['incomes'].append(float(monthly_income))
        financial_data['expenses'].append(float(monthly_expenses))
        financial_data['saved'].append(float(monthly_income - monthly_expenses - monthly_savings))

        # Move to the next month
        if month == 12:
            start_date = datetime(year + 1, 1, 1)
        else:
            start_date = datetime(year, month + 1, 1)

    return financial_data
