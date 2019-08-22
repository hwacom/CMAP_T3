package com.cmap.plugin.module.netflow.statistics;

import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(
		name = "module_ip_traffic_statistics",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {"id"})
		}
)
public class ModuleIpTrafficStatistics {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "group_id", nullable = false)
	private String groupId;

	@Column(name = "stat_date", nullable = false)
    private Date statDate;

	@Column(name = "ip_address", nullable = false)
	private String ipAddress;

	@Column(name = "total_traffic", nullable = true)
    private Long totalTraffic = new Long(0);

	@Column(name = "upload_traffic", nullable = true)
	private Long uploadTraffic = new Long(0);

	@Column(name = "download_traffic", nullable = true)
    private Long downloadTraffic = new Long(0);

	@Column(name = "create_time", nullable = false)
	private Timestamp createTime;

	@Column(name = "create_by", nullable = false)
	private String createBy;

	@Column(name = "update_time", nullable = false)
	private Timestamp updateTime;

	@Column(name = "update_by", nullable = false)
	private String updateBy;

	public ModuleIpTrafficStatistics() {
		super();
	}

    public ModuleIpTrafficStatistics(Integer id, String groupId, Date statDate, String ipAddress,
            Long totalTraffic, Long uploadTraffic, Long downloadTraffic, Timestamp createTime,
            String createBy, Timestamp updateTime, String updateBy) {
        super();
        this.id = id;
        this.groupId = groupId;
        this.statDate = statDate;
        this.ipAddress = ipAddress;
        this.totalTraffic = totalTraffic;
        this.uploadTraffic = uploadTraffic;
        this.downloadTraffic = downloadTraffic;
        this.createTime = createTime;
        this.createBy = createBy;
        this.updateTime = updateTime;
        this.updateBy = updateBy;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Date getStatDate() {
        return statDate;
    }

    public void setStatDate(Date statDate) {
        this.statDate = statDate;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Long getTotalTraffic() {
        return totalTraffic;
    }

    public void setTotalTraffic(Long totalTraffic) {
        this.totalTraffic = totalTraffic;
    }

    public Long getUploadTraffic() {
        return uploadTraffic;
    }

    public void setUploadTraffic(Long uploadTraffic) {
        this.uploadTraffic = uploadTraffic;
    }

    public Long getDownloadTraffic() {
        return downloadTraffic;
    }

    public void setDownloadTraffic(Long downloadTraffic) {
        this.downloadTraffic = downloadTraffic;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
}
