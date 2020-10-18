package com.goodboy.telegram.bot.core.client.uni;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class OnMultipartRequest {

    private String uploadName;
}
