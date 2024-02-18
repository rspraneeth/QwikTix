package com.qwiktix.response;

import com.qwiktix.data.Event;
import com.qwiktix.data.WishItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class WishlistUserResponse {
    private List<WishItem> items;
}
