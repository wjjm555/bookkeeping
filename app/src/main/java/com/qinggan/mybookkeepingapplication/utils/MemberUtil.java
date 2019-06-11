package com.qinggan.mybookkeepingapplication.utils;

import android.support.annotation.IntDef;

import com.qinggan.mybookkeepingapplication.model.Member;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MemberUtil {

    @IntDef({MemberID.CHEN, MemberID.ALICE, MemberID.LONG, MemberID.AI, MemberID.DOCTOR, MemberID.ZP, MemberID.DI})
    @Retention(RetentionPolicy.SOURCE)
    public @interface MembersID {
    }

    public static class MemberID {
        public static final int CHEN = 0, ALICE = 1, LONG = 2, DI = 3, AI = 4, DOCTOR = 5, ZP = 6;
    }

    private final String[] MembersName = {"建铭", "Alice", "晓龙", "宇迪", "艾哥", "医生", "扎鹏"};

    private List<String> MemberNameList = new ArrayList<>(Arrays.asList(MembersName));
    private List<Member> memberList = new ArrayList<>();

    private static class InnerHolder {
        private static final MemberUtil INSTANCE = new MemberUtil();
    }

    public static MemberUtil getInstance() {
        return MemberUtil.InnerHolder.INSTANCE;
    }

    private MemberUtil() {
        for (int i = 0; i < MembersName.length; ++i) {
            Member member = new Member(i, MembersName[i]);
            memberList.add(member);
        }
    }

    public Member getMemberById(int id) {
        return memberList.get(id);
    }

    public Member getMemberByName(String name) {
        return memberList.get(MemberNameList.indexOf(name));
    }

    public List<Member> getMemberList() {
        return memberList;
    }

}
