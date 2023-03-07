package com.popularity.report.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class PDFModel implements Serializable {


    private ImageData imageData;
    private Integer count;
}
