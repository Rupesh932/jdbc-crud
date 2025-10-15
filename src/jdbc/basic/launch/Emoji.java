package jdbc.basic.launch;

public class Emoji {
    
	 	   public static class Status {
	            public static final String SUCCESS       = "\u2705";     // ✅
	            public static final String SUCCESS_ALT   = "\uD83C\uDF89"; // 🎉
	            public static final String ERROR         = "\u274C";     // ❌
	            public static final String FAIL_ALT      = "\uD83D\uDEAB"; // 🚫
	            public static final String WARNING       = "\u26A0";     // ⚠️
	            public static final String WARNING_ALT   = "\uD83D\uDEA8"; // 🚨
	            public static final String INFO          = "\u2139";     // ℹ️
	            public static final String INFO_ALT      = "\uD83D\uDCA1"; // 💡
	            public static final String QUESTION      = "\u2753";     // ❓
	            public static final String THINKING      = "\uD83E\uDD14"; // 🤔
	            public static final String CHECKMARK     = "\u2714";     // ✔️
	            public static final String CROSSMARK     = "\u2716";     // ✖️
	            public static final String EXIT          = "\u274E";     // ❎
	            public static final String LOADING       = "\u23F3";     // ⏳
	        }

	        public static class Action {
	            public static final String OK_HAND       = "\uD83D\uDC4C"; // 👌
	            public static final String THUMBS_UP     = "\uD83D\uDC4D"; // 👍
	            public static final String THUMBS_DOWN   = "\uD83D\uDC4E"; // 👎
	            public static final String HAMMER        = "\uD83D\uDD28"; // 🔨
	            public static final String FIXED         = "\uD83D\uDEE0"; // 🛠️
	            public static final String BUG           = "\uD83D\uDC1B"; // 🐛
	            public static final String FIRE          = "\uD83D\uDD25"; // 🔥
	            public static final String ROCKET        = "\uD83D\uDE80"; // 🚀
	            public static final String SPARKLES      = "\u2728";       // ✨
	        }

	        public static class FileOps {
	            public static final String DATABASE      = "\uD83D\uDCBE"; // 💾
	            public static final String TABLE         = "\uD83D\uDCC4"; // 📄
	            public static final String FILE          = "\uD83D\uDCC3"; // 📃
	            public static final String FOLDER        = "\uD83D\uDCC1"; // 📁
	            public static final String NOTE          = "\uD83D\uDCDD"; // 📝
	            public static final String LINK          = "\uD83D\uDD17"; // 🔗
	            public static final String TRASH         = "\uD83D\uDDD1"; // 🗑️
	            public static final String CLEANUP       = "\uD83E\uDEB5"; // 🧵
	            public static final String RECYCLE       = "\u267B";       // ♻️
	            public static final String ARCHIVE       = "\uD83D\uDCC3"; // 📃
	        }

	        public static class Security {
	            public static final String LOCK          = "\uD83D\uDD12"; // 🔒
	            public static final String UNLOCK        = "\uD83D\uDD13"; // 🔓
	            public static final String KEY           = "\uD83D\uDD11"; // 🔑
	        }

	        public static class UI {
	            public static final String INPUT         = "\uD83D\uDD0D"; // 🔍
	            public static final String CLOCK         = "\u23F0";       // ⏰
	            public static final String STAR          = "\u2B50";       // ⭐
	            public static final String WRITING       = "\u270D";       // ✍️
	            public static final String DESIGN        = "\uD83C\uDFA8"; // 🎨
	            public static final String GLASSES      = "\uD83D\uDC53"; // 👓
	            public static final String OPEN_BOOK    = "\uD83D\uDCD6"; // 📖
	            public static final String MENU = "\uD83D\uDCCB"; // 📋
	            public static final String PREVIEW = "\uD83D\uDCCB"; // 🗋 or 🧾 

	        }

	        public static class Navigation {
	            public static final String NEXT          = "\u25B6";       // ▶️
	            public static final String PREVIOUS      = "\u25C0";       // ◀️
	            public static final String UP_ARROW      = "\u2B06";       // ⬆️
	            public static final String DOWN_ARROW    = "\u2B07";       // ⬇️
	            public static final String RIGHT_ARROW   = "\u27A1";       // ➡️
	            public static final String LEFT_ARROW    = "\u2B05";       // ⬅️
	            public static final String REPEAT        = "\uD83D\uDD04"; // 🔄
	            public static final String REFRESH       = "\uD83D\uDD04"; // 🔄
	        }

	        public static class Identity {
	            public static final String USER          = "\uD83D\uDC64"; // 👤
	            public static final String GROUP         = "\uD83D\uDC65"; // 👥
	            public static final String ADMIN         = "\uD83D\uDC68\u200D\uD83D\uDCBB"; // 👨‍💻
	            public static final String GUEST         = "\uD83D\uDC68\u200D\uD83D\uDCBC"; // 👨‍💼
	            public static final String ROBOT         = "\uD83E\uDD16"; // 🤖
	        }

	        public static class Network {
	            public static final String WIFI          = "\uD83D\uDCF6"; // 📶
	            public static final String ANTENNA       = "\uD83D\uDCE1"; // 📡
	            public static final String SERVER        = "\uD83D\uDCBB"; // 💻
	            public static final String CLOUD         = "\u2601";       // ☁️
	            public static final String GLOBE         = "\uD83C\uDF10"; // 🌐
	        }

	        public static class Testing {
	            public static final String TEST          = "\uD83D\uDD2C"; // 🔬
	            public static final String EXPERIMENT    = "\u2697";       // ⚗️
	            public static final String LOG           = "\uD83D\uDCC2"; // 📂
	            public static final String TRACE         = "\uD83D\uDD0E"; // 🔎
	            public static final String BREAKPOINT    = "\uD83D\uDEA9"; // 🚩
	        }

	        public static class Build {
	            public static final String PACKAGE       = "\uD83D\uDCE6"; // 📦
	            public static final String DEPLOY        = "\uD83D\uDEEB"; // 🛫
	            public static final String INSTALL       = "\uD83D\uDEE0"; // 🛠️
	            public static final String UPDATE        = "\uD83D\uDD04"; // 🔄
	            public static final String VERSION       = "\uD83D\uDCC5"; // 📅
	        }
	    }


	


