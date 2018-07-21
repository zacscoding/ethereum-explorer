package org.explorer;

import java.util.ArrayList;
import java.util.List;
import org.explorer.dto.PageListRequest;
import org.junit.Test;

/**
 * @author zacconding
 * @Date 2018-07-21
 * @GitHub : https://github.com/zacscoding
 */
public class Temp {

    @Test
    public void rest() {
        List<Integer> arr = new ArrayList<>();
        arr.add(0);
        System.out.println(arr.subList(0,0).size());
    }

    private int getOffset(PageListRequest pageRequest) {
        return Math.min(0, (pageRequest.getStart() - 1) * pageRequest.getLength() - pageRequest.getLength());
    }

}
