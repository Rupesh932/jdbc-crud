package jdbc.basic.launch;

public class ServiceMenu {
	public static void mainMenu() {
		System.out.println();
		String menuText = StyledMessage.Banner.menu("  MY SERVICES     ", """
				 \t 1. CREATE TABLE
				 \t 2. INSERT DATA
				 \t 3. READ DATA
				 \t 4. UPDATE DATA
				 \t 5. DELETE(delete row/s)
				 \t 6. DROP TABLE
				 \t 7. DROP DATABASE
				 \t 8. ALTER TABLE(add/drop/modify column)
				 \t 9. EXIT.
				""");
		System.out.println(menuText);
	}

	public static void alterTableSubMenu() {
		System.out.println();
		String subMenu = StyledMessage.Banner.menu("  Alter Table Options", """
				 \t\t 1. Add Column
				 \t 2. Drop Column
				 \t 3. Modify Column(type and constrain)
				 \t 0. Back
				 		""");
		System.out.println(subMenu);

	}
	public static void renderModifyColumnMenu() {
	    System.out.println();
	    String subMenu = StyledMessage.Banner.menu(" Column Modification Options", """
	           \t 1. Rename Column
	           \t 2. Change Data Type
	           \t 3. Modify Key Constraint
	           \t 4. Toggle Nullability
	           \t 5. Update Extra Flags
	           \t 0. Back
	    """);
	    System.out.println(subMenu);
	}

}
