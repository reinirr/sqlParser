import Entity.Query;
import services.QueryParser;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        var sqlQuery = "SELECT column1, column2 FROM table1 WHERE column3 = 5 GROUP BY column4 ORDER BY column2 LIMIT 10 OFFSET 20";
        Query query = QueryParser.parse(sqlQuery);
        System.out.println(query);

        System.out.println("----------------------------");
        sqlQuery = "SELECT author.name, count(book.id), sum(book.cost) " +
                "FROM author " +
                "LEFT JOIN book ON (author.id = book.author_id) " +
                "GROUP BY author.name " +
                "HAVING COUNT(*) > 1 AND SUM(book.cost) > 500 " +
                "LIMIT 10;";
        Query query2 = QueryParser.parse(sqlQuery);
        System.out.println(query2);


        var scanner = new Scanner(System.in);
        var inputQuery = scanner.nextLine();
        while(!inputQuery.isEmpty()) {
            System.out.println("----------------------------");
            Query parsedQuery = QueryParser.parse(inputQuery);
            System.out.println(parsedQuery);
            inputQuery = scanner.nextLine();
        }

        System.out.println("END");
    }
}
