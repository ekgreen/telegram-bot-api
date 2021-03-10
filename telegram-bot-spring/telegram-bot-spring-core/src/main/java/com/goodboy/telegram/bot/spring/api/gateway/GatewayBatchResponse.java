package com.goodboy.telegram.bot.spring.api.gateway;

import com.goodboy.telegram.bot.api.Update;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Optional;

@Data
@Accessors(chain = true)
public class GatewayBatchResponse {

    // batch complete result
    private final Status status;

    // not routed updates
    private List<Fail> failed;

    /**
     * return boolean status of request
     *
     * @return success\partly failed
     */
    public boolean isOk() {
        return status == Status.SUCCESS;
    }


    /**
     * extra logic on incomplete result - do some logic on fail
     *
     * @return optional with failed updates ids
     */
    public Optional<List<Fail>> onFail() {
        return isOk() ? Optional.empty() : Optional.ofNullable(failed);
    }


    /**
     * @return count of failed updates
     */
    public int failedCount() {
        return failed == null ? 0 : failed.size();
    }

    /**
     * fail reason and id
     */
    @Data
    public static class Fail {
        private final long code;
        private final Update update;
    }

    /**
     * available batch set of statuses
     */
    public static enum Status {
        SUCCESS, INCOMPLETE_ROUTE, FAILED
    }
}
