package model;

import DAO.DataTransferObject;

public class PlatformInfo implements DataTransferObject  {

    private long platformInfoId;
    private long personId;
    private String handle;
    private String platform;

    public PlatformInfo(){}

    public PlatformInfo(String handle, String platform) {
        this.handle = handle;
        this.platform = platform;
    }

    public long getPlatformInfoId() {
        return platformInfoId;
    }

    public void setPlatformInfoId(long platformInfoId) {
        this.platformInfoId = platformInfoId;
    }

    public long getPersonId() {
        return personId;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    @Override
    public long getId() {
        return 0;
    }
}
