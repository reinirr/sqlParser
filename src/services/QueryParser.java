package services;

import Entity.Query;
import Entity.WhereClause;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryParser {
    public static Query parse(String sqlQuery) {
        var query = new Query();

        Pattern selectPattern = Pattern.compile("SELECT (.+?) FROM", Pattern.CASE_INSENSITIVE);
        Pattern fromPattern = Pattern.compile("FROM (.+?)(?: WHERE| GROUP BY| ORDER BY| LIMIT| OFFSET|$)", Pattern.CASE_INSENSITIVE);
        Pattern wherePattern = Pattern.compile("(?:WHERE|HAVING) (.+?)(?: GROUP BY| ORDER BY| LIMIT| OFFSET|$)", Pattern.CASE_INSENSITIVE);
        Pattern groupByPattern = Pattern.compile("GROUP BY (.+?)(?: ORDER BY| LIMIT| OFFSET|$)", Pattern.CASE_INSENSITIVE);
        Pattern orderByPattern = Pattern.compile("ORDER BY (.+?)(?: LIMIT| OFFSET|$)", Pattern.CASE_INSENSITIVE);
        Pattern limitPattern = Pattern.compile("LIMIT (\\d+)", Pattern.CASE_INSENSITIVE);
        Pattern offsetPattern = Pattern.compile("OFFSET (\\d+)", Pattern.CASE_INSENSITIVE);


        Matcher selectMatcher = selectPattern.matcher(sqlQuery);
        if (selectMatcher.find()) {
            String selectFields = selectMatcher.group(1);
            query.setColumns(parseColumns(selectFields));
        }

        Matcher fromMatcher = fromPattern.matcher(sqlQuery);
        if (fromMatcher.find()) {
            var fromSources = fromMatcher.group(1);
            query.setFromSources(parseFromSources(fromSources));
        }

        Matcher groupByMatcher = groupByPattern.matcher(sqlQuery);
        if (groupByMatcher.find()) {
            String groupByFields = groupByMatcher.group(1);
            query.setGroupByColumns(parseColumns(groupByFields));
        }

        Matcher orderByMatcher = orderByPattern.matcher(sqlQuery);
        if (orderByMatcher.find()) {
            String orderByFields = orderByMatcher.group(1);
            query.setSortColumns(parseSortColumns(orderByFields));
        }

        Matcher whereMatcher = wherePattern.matcher(sqlQuery);
        if (whereMatcher.find()) {
            String whereFields = whereMatcher.group(1);
            query.setWhereClauses(parseWhereClauses(whereFields));
        }

        Matcher limitMatcher = limitPattern.matcher(sqlQuery);
        if (limitMatcher.find()) {
            int limit = Integer.parseInt(limitMatcher.group(1));
            query.setLimit(limit);
        }

        Matcher offsetMatcher = offsetPattern.matcher(sqlQuery);
        if (offsetMatcher.find()) {
            int offset = Integer.parseInt(offsetMatcher.group(1));
            query.setOffset(offset);
        }

        return query;
    }

    private static List<String> parseColumns(String columns) {
        var columnsArray = columns.split(",");
        List<String> columnsList = new ArrayList<>();
        for (var column : columnsArray) {
            columnsList.add(column.trim());
        }
        return columnsList;
    }

    private static List<String> parseFromSources(String fromSources) {
        var sourcesArray = fromSources.split(",");
        List<String> sourcesList = new ArrayList<>();
        for (var source : sourcesArray) {
            sourcesList.add(source.trim());
        }
        return sourcesList;
    }

    private static List<WhereClause> parseWhereClauses(String whereClauses) {
        var whereClausesArray = whereClauses.split("\\s*(?:AND|,|OR)\\s*");
        List<WhereClause> clauses = new ArrayList<>();
        for (String clause : whereClausesArray) {
            String[] clauseParts = clause.split("\\s*(?<=(?:!=|=|>|<|>=|<=))\\s*|\\s*(?=(?:!=|=|>|<|>=|<=))");
            clauseParts = Arrays.stream(clauseParts)
                    .map(String::trim)
                    .filter(str -> !str.isEmpty())
                    .toArray(String[]::new);
            if (clauseParts.length != 3) {
                // обрабатываем неправильно отформатированные клозы, бросаем исключение или выполняем другие действия
                // например, можно внести запись в журнал и продолжить обработку остальных клозов
                continue; // пропускаем текущий цикл и переходим к следующему
            }
            String column = clauseParts[0].trim();
            String operator = clauseParts[1];
            String value = clauseParts[2];
            clauses.add(new WhereClause(column, operator, value));
        }
        return clauses;
    }

    private static List<String> parseSortColumns(String orderByFields) {
        var sortColumnsArray = orderByFields.split(",");
        List<String> sortColumnsList = new ArrayList<>();
        for (var sortColumn : sortColumnsArray) {
            sortColumnsList.add(sortColumn.trim());
        }
        return sortColumnsList;
    }
}
