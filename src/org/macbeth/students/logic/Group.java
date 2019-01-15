package org.macbeth.students.logic;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Group {

    private int groupId;
    private String groupName;
    private String curator;
    private String speciality;

    public Group(ResultSet resultSet) throws SQLException {

        groupId = resultSet.getInt("group_id");
        groupName = resultSet.getString("group_name");
        curator = resultSet.getString("curator");
        speciality = resultSet.getString("speciality");
    }

    public Group(int groupId, String groupName, String curator, String speciality) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.curator = curator;
        this.speciality = speciality;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getCurator() {
        return curator;
    }

    public void setCurator(String curator) {
        this.curator = curator;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    @Override
    public String toString() {
        return groupName+"/"+speciality+"/"+curator;
    }
}
