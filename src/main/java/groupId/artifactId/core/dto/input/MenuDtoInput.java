package groupId.artifactId.core.dto.input;

public class MenuDtoInput {

    private String name;
    private Boolean enable;

    public MenuDtoInput() {
    }

    public MenuDtoInput(String name, Boolean enable) {
        this.name = name;
        this.enable = enable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    @Override
    public String toString() {
        return "MenuDtoInput{" +
                "name='" + name + '\'' +
                ", enable=" + enable +
                '}';
    }
}
