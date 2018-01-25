package com.li.videoapplication.data.model.response;

import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;

/**
 * Created by cx on 2018/1/24.
 */

public class Vip3AndAuthoryEntity extends BaseResponseEntity {
    /**
     * privilegeData : {"memberVipLevel":"1","privilegeText":[{"text":"加速审核","highlighted":false},{"text":"高清转码","highlighted":false},{"text":"时长无限制","highlighted":true},{"text":"大小无限制","highlighted":true}]}
     * diamondVipData : {"diamondVipLevel":"3","diamondVipPrice":20,"diamondVipText":[{"mainText":{"title":"加速审核","color":"#66666","fontSize":"14"},"secondText":{"title":"专属人工审核，提速","color":"#99999","fontSize":"12"},"thirdText":{"title":"90%","color":"#ff3d2e","fontSize":"12"}},{"mainText":{"title":"高清转码","color":"#66666","fontSize":"14"},"secondText":{"title":"视频更清晰","color":"#99999","fontSize":"12"}},{"mainText":{"title":"屏蔽广告","color":"#66666","fontSize":"14"}},{"mainText":{"title":"秒转码","color":"#66666","fontSize":"14"}},{"mainText":{"title":"分享视频时长无限制","color":"#66666","fontSize":"14"}},{"mainText":{"title":"分享视频大小无限制","color":"#66666","fontSize":"14"}},{"mainText":{"title":"生活类视频可以在线分享","color":"#66666","fontSize":"14"}}],"packageMemu":[{"key":1,"text":"一个月","discount":1},{"key":3,"text":"三个月","discount":0.9},{"key":6,"text":"六个月","discount":0.85},{"key":12,"text":"十二个月","discount":0.8}]}
     */

    private PrivilegeDataBean privilegeData;
    private DiamondVipDataBean diamondVipData;

    public PrivilegeDataBean getPrivilegeData() {
        return privilegeData;
    }

    public void setPrivilegeData(PrivilegeDataBean privilegeData) {
        this.privilegeData = privilegeData;
    }

    public DiamondVipDataBean getDiamondVipData() {
        return diamondVipData;
    }

    public void setDiamondVipData(DiamondVipDataBean diamondVipData) {
        this.diamondVipData = diamondVipData;
    }

    public static class PrivilegeDataBean {
        /**
         * memberVipLevel : 1
         * privilegeText : [{"text":"加速审核","highlighted":false},{"text":"高清转码","highlighted":false},{"text":"时长无限制","highlighted":true},{"text":"大小无限制","highlighted":true}]
         */

        private String memberVipLevel;
        private List<PrivilegeTextBean> privilegeText;

        public String getMemberVipLevel() {
            return memberVipLevel;
        }

        public void setMemberVipLevel(String memberVipLevel) {
            this.memberVipLevel = memberVipLevel;
        }

        public List<PrivilegeTextBean> getPrivilegeText() {
            return privilegeText;
        }

        public void setPrivilegeText(List<PrivilegeTextBean> privilegeText) {
            this.privilegeText = privilegeText;
        }

        public static class PrivilegeTextBean {
            /**
             * text : 加速审核
             * highlighted : false
             */

            private String text;
            private boolean highlighted;

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public boolean isHighlighted() {
                return highlighted;
            }

            public void setHighlighted(boolean highlighted) {
                this.highlighted = highlighted;
            }
        }
    }

    public static class DiamondVipDataBean {
        /**
         * diamondVipLevel : 3
         * diamondVipPrice : 20
         * diamondVipText : [{"mainText":{"title":"加速审核","color":"#66666","fontSize":"14"},"secondText":{"title":"专属人工审核，提速","color":"#99999","fontSize":"12"},"thirdText":{"title":"90%","color":"#ff3d2e","fontSize":"12"}},{"mainText":{"title":"高清转码","color":"#66666","fontSize":"14"},"secondText":{"title":"视频更清晰","color":"#99999","fontSize":"12"}},{"mainText":{"title":"屏蔽广告","color":"#66666","fontSize":"14"}},{"mainText":{"title":"秒转码","color":"#66666","fontSize":"14"}},{"mainText":{"title":"分享视频时长无限制","color":"#66666","fontSize":"14"}},{"mainText":{"title":"分享视频大小无限制","color":"#66666","fontSize":"14"}},{"mainText":{"title":"生活类视频可以在线分享","color":"#66666","fontSize":"14"}}]
         * packageMemu : [{"key":1,"text":"一个月","discount":1},{"key":3,"text":"三个月","discount":0.9},{"key":6,"text":"六个月","discount":0.85},{"key":12,"text":"十二个月","discount":0.8}]
         */

        private String diamondVipLevel;
        private int diamondVipPrice;
        private List<DiamondVipTextBean> diamondVipText;
        private List<PackageMemuBean> packageMemu;

        public String getDiamondVipLevel() {
            return diamondVipLevel;
        }

        public void setDiamondVipLevel(String diamondVipLevel) {
            this.diamondVipLevel = diamondVipLevel;
        }

        public int getDiamondVipPrice() {
            return diamondVipPrice;
        }

        public void setDiamondVipPrice(int diamondVipPrice) {
            this.diamondVipPrice = diamondVipPrice;
        }

        public List<DiamondVipTextBean> getDiamondVipText() {
            return diamondVipText;
        }

        public void setDiamondVipText(List<DiamondVipTextBean> diamondVipText) {
            this.diamondVipText = diamondVipText;
        }

        public List<PackageMemuBean> getPackageMemu() {
            return packageMemu;
        }

        public void setPackageMemu(List<PackageMemuBean> packageMemu) {
            this.packageMemu = packageMemu;
        }

        public static class DiamondVipTextBean {
            /**
             * mainText : {"title":"加速审核","color":"#66666","fontSize":"14"}
             * secondText : {"title":"专属人工审核，提速","color":"#99999","fontSize":"12"}
             * thirdText : {"title":"90%","color":"#ff3d2e","fontSize":"12"}
             */

            private MainTextBean mainText;
            private SecondTextBean secondText;
            private ThirdTextBean thirdText;

            public MainTextBean getMainText() {
                return mainText;
            }

            public void setMainText(MainTextBean mainText) {
                this.mainText = mainText;
            }

            public SecondTextBean getSecondText() {
                return secondText;
            }

            public void setSecondText(SecondTextBean secondText) {
                this.secondText = secondText;
            }

            public ThirdTextBean getThirdText() {
                return thirdText;
            }

            public void setThirdText(ThirdTextBean thirdText) {
                this.thirdText = thirdText;
            }

            public static class MainTextBean {
                /**
                 * title : 加速审核
                 * color : #66666
                 * fontSize : 14
                 */

                private String title;
                private String color;
                private String fontSize;

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getColor() {
                    return color;
                }

                public void setColor(String color) {
                    this.color = color;
                }

                public String getFontSize() {
                    return fontSize;
                }

                public void setFontSize(String fontSize) {
                    this.fontSize = fontSize;
                }
            }

            public static class SecondTextBean {
                /**
                 * title : 专属人工审核，提速
                 * color : #99999
                 * fontSize : 12
                 */

                private String title;
                private String color;
                private String fontSize;

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getColor() {
                    return color;
                }

                public void setColor(String color) {
                    this.color = color;
                }

                public String getFontSize() {
                    return fontSize;
                }

                public void setFontSize(String fontSize) {
                    this.fontSize = fontSize;
                }
            }

            public static class ThirdTextBean {
                /**
                 * title : 90%
                 * color : #ff3d2e
                 * fontSize : 12
                 */

                private String title;
                private String color;
                private String fontSize;

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getColor() {
                    return color;
                }

                public void setColor(String color) {
                    this.color = color;
                }

                public String getFontSize() {
                    return fontSize;
                }

                public void setFontSize(String fontSize) {
                    this.fontSize = fontSize;
                }
            }
        }

        public static class PackageMemuBean {
            /**
             * key : 1
             * text : 一个月
             * discount : 1
             */

            private int key;
            private String text;
            private float discount;

            public int getKey() {
                return key;
            }

            public void setKey(int key) {
                this.key = key;
            }

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public float getDiscount() {
                return discount;
            }

            public void setDiscount(float discount) {
                this.discount = discount;
            }
        }
    }
}
