package groupId.artifactId.core.mapper;

import groupId.artifactId.core.dto.output.OrderStageDtoOutput;
import groupId.artifactId.dao.entity.OrderStage;
import groupId.artifactId.dao.entity.api.IOrderStage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

@ExtendWith(MockitoExtension.class)
class OrderStageMapperTest {
    @InjectMocks
    private OrderStageMapper orderStageMapper;

    @Test
    void inputMapping() {
        // preconditions
        final String description = "Order accepted";

        //test
        IOrderStage test = orderStageMapper.inputMapping(description);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(description, test.getDescription());
    }

    @Test
    void outputMapping() {
        // preconditions
        final long id = 1L;
        final String stageDescription = "Stage #";
        final Instant creationDate = Instant.now();
        IOrderStage orderStages = OrderStage.builder().id(id).description(stageDescription).creationDate(creationDate).build();

        //test
        OrderStageDtoOutput test = orderStageMapper.outputMapping(orderStages);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(stageDescription, test.getDescription());
        Assertions.assertEquals(creationDate, test.getCreatedAt());
    }
}