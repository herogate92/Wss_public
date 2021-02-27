package herogate.websocket.domain;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
@Entity
public class Con_log {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;

	@Column(length = 1000, nullable = true)
	private String header;

	@Column(length = 200, nullable = true)
	private String msg;

	@Column(length = 50, nullable = true)
	private String ipaddr;

	@Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Date datetime;

	@Builder
	public Con_log(String header, String msg, String ipaddr) {
		this.header = header;
		this.msg = msg;
		this.ipaddr = ipaddr;
	}
}
