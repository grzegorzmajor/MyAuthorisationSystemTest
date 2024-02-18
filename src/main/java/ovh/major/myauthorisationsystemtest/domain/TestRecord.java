package ovh.major.myauthorisationsystemtest.domain;

import lombok.Builder;

@Builder
public record TestRecord(
        //Long id,
        String description,
        String somethingElse
) {
}
