package de.abas.training.basic.objects;

import de.abas.erp.db.DbContext;
import de.abas.erp.db.EditorAction;
import de.abas.erp.db.Query;
import de.abas.erp.db.exception.CommandException;
import de.abas.erp.db.schema.part.Product;
import de.abas.erp.db.schema.part.ProductEditor;
import de.abas.erp.db.selection.Conditions;
import de.abas.erp.db.selection.SelectionBuilder;
import de.abas.training.basic.common.AbstractAjoAccess;

/**
 * Editing sales price of existing product.
 *
 * @author abas Software AG
 *
 */
public class EditExistingProducts extends AbstractAjoAccess {

	DbContext ctx = null;

	@Override
	public void run(String[] args) {
		ctx = getDbContext();

		for (Product product : getProductsBetween("10150", "10160")) {
			try {
				ProductEditor productEditor = product.createEditor();
				productEditor.open(EditorAction.UPDATE);
				// increases sales price by 10 percent
				double newSalesPrice = productEditor.getSalesPrice().doubleValue() * 1.10;
				productEditor.setSalesPrice(newSalesPrice);
				productEditor.commit();
				ctx.out().println("Sales price of " + product.getIdno() + " " + product.getSwd() + " changed: " + product.getSalesPrice());
			}
			catch (CommandException e) {
				ctx.out().println("An exception occurred while trying to edit " + product.getId().toString() + ": " + e.getMessage());
			}
		}
	}

	/**
	 * Selects products in defined idno range.
	 *
	 * @param from Start idno.
	 * @param to End idno.
	 * @return Iterable Query object containing the selected products.
	 */
	private Query<Product> getProductsBetween(String from, String to) {
		SelectionBuilder<Product> selectionBuilder = SelectionBuilder.create(Product.class);
		selectionBuilder.add(Conditions.between(Product.META.idno, from, to));
		Query<Product> queryProduct = getDbContext().createQuery(selectionBuilder.build());
		return queryProduct;
	}

}