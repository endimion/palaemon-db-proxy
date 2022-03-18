package gr.uaegean.palaemondbproxy.model;

import lombok.*;
import org.springframework.data.elasticsearch.annotations.Field;

import static org.springframework.data.elasticsearch.annotations.FieldType.Text;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TicketInfo {

    @Field(type = Text)
    private String name;
    @Field(type = Text)
    private String surname;
    @Field(type = Text)
    private String dateOfBirth;
    @Field(type = Text)
    private String gender;
    @Field(type = Text)
    private String ticketNumber;

}