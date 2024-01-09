package cl.duoc.mgaray.guardalotwo.service.cmd;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DeleteProductCmd {
  private long id;
}
