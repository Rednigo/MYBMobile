from datetime import datetime
from sqlalchemy import create_engine, Column, Integer, String, DateTime, Float, ForeignKey, Boolean
from sqlalchemy.orm import declarative_base, relationship, sessionmaker

from core.config import DB_USER, DB_PASS, DB_HOST, DB_PORT, DB_NAME
from core.settings import database_url


# engine = create_engine("mysql://root:1q2w3e4r@localhost:3306/myb_mobile", echo=True)
engine = create_engine(f"mysql://{DB_USER}:{DB_PASS}@{DB_HOST}:{DB_PORT}/{DB_NAME}", echo=True)
Base = declarative_base()
metadata = Base.metadata
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)


class User(Base):
    __tablename__ = "user"
    id = Column(Integer, primary_key=True, index=True)
    username = Column(String(255), unique=True, nullable=False)
    email = Column(String(255), unique=True, nullable=False)
    hashed_password = Column(String(255), nullable=False)
    is_light_theme = Column(Boolean, default=True)
    language = Column(String(50), default="uk")

    # Relationships
    incomes = relationship('Income', back_populates='user')
    savings = relationship('Savings', back_populates='user')
    categories = relationship('ExpenseCategory', back_populates='user')  # Link to ExpenseCategory


class Income(Base):
    __tablename__ = "income"
    id = Column(Integer, primary_key=True, index=True)
    income_name = Column(String(255), nullable=False)
    amount = Column(Float, nullable=False)
    date = Column(DateTime, default=datetime.utcnow)
    user_id = Column(Integer, ForeignKey('user.id'), nullable=False)

    user = relationship('User', back_populates='incomes')


class Savings(Base):
    __tablename__ = "savings"
    id = Column(Integer, primary_key=True, index=True)
    savings_name = Column(String(255), nullable=False)
    amount = Column(Float, nullable=False)
    date = Column(DateTime, default=datetime.utcnow)
    user_id = Column(Integer, ForeignKey('user.id'), nullable=False)

    user = relationship('User', back_populates='savings')


class ExpenseCategory(Base):
    __tablename__ = "expense_category"
    id = Column(Integer, primary_key=True, index=True)
    category_name = Column(String(255), nullable=False)
    amount = Column(Float, nullable=False)
    user_id = Column(Integer, ForeignKey('user.id'), nullable=False)

    user = relationship('User', back_populates='categories')
    expenses = relationship('Expense', back_populates='category', cascade="all, delete-orphan")


class Expense(Base):
    __tablename__ = "expense"
    id = Column(Integer, primary_key=True, index=True)
    expense_name = Column(String(255), nullable=False)
    amount = Column(Float, nullable=False)
    date = Column(DateTime, default=datetime.utcnow)
    category_id = Column(Integer, ForeignKey('expense_category.id', ondelete="CASCADE"))

    category = relationship('ExpenseCategory', back_populates='expenses')
