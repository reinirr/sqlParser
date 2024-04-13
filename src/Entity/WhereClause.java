package Entity;

public class WhereClause {
    private String column;
    private String operator;
    private Object value;
    public WhereClause(String column, String operator, Object value) {
        this.column = column;
        this.value = value;
        this.operator = operator;
    }


    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }


    @Override
    public String toString() {
        return "[" +  column + " " + operator + " " + value + "]";
    }


}
