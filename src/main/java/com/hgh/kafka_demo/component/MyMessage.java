package com.hgh.kafka_demo.component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyMessage implements Serializable {

    private String id;

    private String message;

}
