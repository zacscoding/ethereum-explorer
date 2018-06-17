package org.explorer.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.explorer.entity.annotation.ValidateMe;

/**
 * @author zacconding
 * @Date 2018-06-17
 * @GitHub : https://github.com/zacscoding
 */

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PageListRequest {

    /**
     * page
     */
    private int start;
    /**
     * page size
     */
    private int length;

    @ValidateMe
    public void validatePageRequest() {
        if (start <= 0) {
            start = 1;
        }

        if (length <= 0) {
            length = 10;
        }
    }
}
