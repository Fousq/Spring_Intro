package kz.zhanbolat.spring.storage;

public enum EntityNamespace {
    USER("user"), EVENT("event"), TICKET("ticket");

    private final String namespace;

    EntityNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getNamespace() {
        return namespace;
    }
}
