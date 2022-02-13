package com.tms.api.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "log_token")
public class LogToken implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
    @SequenceGenerator(name="logtoken_generator", sequenceName = "seq_log_token", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "logtoken_generator")
    @Column(name = "id")
    private Integer id;
    @Column(name = "create_date")
    private Date createDate;
    @Column(name = "token")
    private String token;
    @Column(name = "partner_id")
    private String partnerId;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getPartnerId() {
		return partnerId;
	}
	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}
}
