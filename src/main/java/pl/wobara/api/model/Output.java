package pl.wobara.api.model;

import lombok.Getter;

import java.util.List;

@Getter
public class Output {

    private Signature signature;
    private int count;
    private List<String> fields;
    private List<List<String>> data;

}