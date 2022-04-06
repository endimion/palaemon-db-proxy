package gr.uaegean.palaemondbproxy;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class IntegrationTests {

    static String token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJhLXRjZGlUVzBCNGxoR3liY1NsSzRPS0JBNUpUVWhvbGJsd0NtcFFxdU1JIn0.eyJqdGkiOiI3ZTM5ZmZlNi05ZDQ0LTQ4Y2MtOWY0MS01ZjkzMTVjZTYxNjMiLCJleHAiOjE2NDgxMjA2MjIsIm5iZiI6MCwiaWF0IjoxNjQ4MTIwMzIyLCJpc3MiOiJodHRwczovL2RzczEuYWVnZWFuLmdyL2F1dGgvcmVhbG1zL3BhbGFlbW9uIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6IjQxNjYwOGVjLTgzYjQtNDE0Mi05ZDExLWU5MTUxYjUxY2NhMCIsInR5cCI6IkJlYXJlciIsImF6cCI6InBhbGFlbW9uUmVnaXN0cmF0aW9uIiwiYXV0aF90aW1lIjowLCJzZXNzaW9uX3N0YXRlIjoiNDcyNjIwNTctNzRmYi00NmNkLTllOTEtOTdiNmFiZTdlNDA4IiwiYWNyIjoiMSIsInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJvcGVuaWQgcHJvZmlsZSBlbWFpbCIsImNsaWVudEhvc3QiOiIxOTUuMjUxLjE0MS41MyIsImNsaWVudElkIjoicGFsYWVtb25SZWdpc3RyYXRpb24iLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsInByZWZlcnJlZF91c2VybmFtZSI6InNlcnZpY2UtYWNjb3VudC1wYWxhZW1vbnJlZ2lzdHJhdGlvbiIsImNsaWVudEFkZHJlc3MiOiIxOTUuMjUxLjE0MS41MyIsImVtYWlsIjoic2VydmljZS1hY2NvdW50LXBhbGFlbW9ucmVnaXN0cmF0aW9uQHBsYWNlaG9sZGVyLm9yZyJ9.HPII1cwJsPjWWOthKEK2G1JDnawDDR4lMsDZC0DGkeEQFEdIzwkd6LygI3r2N8v1Zzz6hBbNg5bt_-GHOQmtlC6oNgjPivya266S3ZyrqFlZTtPLsL0C5NXoUvyOPUngKo-7efGGv-WlT9wpr6YdXuOyEew6IId9SmIXLI2z2SAEZjpjRf9ccU9K84owD6xtEe43HeqW6YHh99fu5lkj7j74zApezCvPdJ_Eemm0JpeCQtmn0-TU6F-OyvXJC9IwCDjeSmV5V6WrEkU2IamJtfjMBm1FjRxi65S1p8ziAJ17CxKLFZa_CWG6niPP4ZXNZzg6UHsh2sJWxPM9SSJDng";

    @Test
    public void testAddPerson() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://dss.aegean.gr:8090/addPerson/"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .method("POST", HttpRequest.BodyPublishers.ofString("{\n    \"name\" : \"CLAUDE\",\n    \"surname\": \"PHIL\",\n    \"identifier\": \"EL/EL/11111\",\n    \"gender\": \"'Male'\",\n    \"age\": \"'1965-01-01'\",\n\t\"preferred_language\":[\"EL\", \"ES\"],\n    \"ticketNumber\": \"'123'\",\n    \"medical_condnitions\": \"'non'\",\n\t\"isCrew\": \"true\",\n\t\"role\": \"captain\"\n  }"))
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());


    }

}
