package com.vibecast.viewservice.model.persistance;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Image implements Serializable {
    @NotNull
    @NotEmpty
    private String url;
    private int height;
    private int width;
}
