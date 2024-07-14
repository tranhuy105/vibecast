package com.vibecast.viewservice.model.response;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class PaginatedObjectDto<T> implements Serializable {
    private int limit;
    private int offset;
    private int total;
    private boolean hasPrevious;
    private boolean hasNext;
    private List<T> items;

    public PaginatedObjectDto(int limit, int offset, int total, List<T> items) {
        this.limit = limit;
        this.offset = offset;
        this.total = total;
        this.items = items;
        this.hasPrevious = offset > 0;
        this.hasNext = offset + limit < total;
    }
}
