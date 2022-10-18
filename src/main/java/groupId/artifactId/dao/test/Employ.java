package groupId.artifactId.dao.test;

public class Employ {
    private Long id;
    private String name;
    private Long jobId;
    private Long departmentId;

    public Employ() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public String toString() {
        return "Employ{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", jobId=" + jobId +
                ", departmentId=" + departmentId +
                '}';
    }
}
