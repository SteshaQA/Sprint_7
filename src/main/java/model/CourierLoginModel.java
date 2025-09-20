package model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CourierLoginModel {

    private String login;
    private String password;

}
