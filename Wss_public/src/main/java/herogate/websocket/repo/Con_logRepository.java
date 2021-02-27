package herogate.websocket.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import herogate.websocket.domain.Con_log;

public interface Con_logRepository extends CrudRepository<Con_log, Long> {
	public List<Con_log> findByIpaddr(String ipaddr);

	public List<Con_log> findByIpaddrLike(String ipaddr);
}