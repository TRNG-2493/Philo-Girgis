"""Tests for calculate_cart_total (class-style tests)."""
import pytest

from qe_utils import calculate_cart_total


# TODO (Task 3/4): put the markers `regression` on the whole class (class-level marker),
# then fill in the methods. Each method is a separate test; `self` is the only extra argument.
@pytest.mark.regression
class TestCartTotal:
    # ---- WORKED EXAMPLE ----
    @pytest.mark.smoke
    def test_total_of_two_items_without_tax(self):
        assert calculate_cart_total([10.00, 5.50]) == 15.50

    # ---- TODO tests ----
    @pytest.mark.smoke
    def test_total_with_ten_percent_tax(self):
        # TODO: [100.00] with tax_rate=0.10 -> 110.0. Marker: smoke
        assert calculate_cart_total([100.00], 0.10) == 110.0

    @pytest.mark.boundary
    def test_empty_cart_total_is_zero(self):
        # TODO: [] -> 0. Marker: boundary
        assert calculate_cart_total([]) == 0

    def test_total_is_rounded_to_two_decimals(self):
        # TODO: [19.99, 0.01, 3.333] with tax_rate=0.07 -> compare with pytest.approx(..., abs=0.01)
        assert calculate_cart_total([19.99, 0.01, 3.333], 0.07) == pytest.approx(24.97, abs=0.01)

    def test_negative_price_raises_value_error(self):
        # TODO (Task 4): [5.0, -1.0] -> ValueError, match="negative"
        with pytest.raises(ValueError, match="negative"):
            calculate_cart_total([5.0, -1.0])

    def test_negative_tax_rate_raises_value_error(self):
        # TODO (Task 4): [5.0] with tax_rate=-0.1 -> ValueError, match="tax_rate"
        with pytest.raises(ValueError, match="tax_rate"):
            calculate_cart_total([5.0], -0.1)

    # TODO (Task 5): add a test named test_large_cart_of_10000_items that totals
    # [1.0] * 10_000 (expect 10000.0). Marker: slow  (it "stands in" for a slow test).
    @pytest.mark.slow
    def test_large_cart_of_10000_items(self):
        assert calculate_cart_total([1.0] * 10_000) == 10000.0


    # TODO (Task 5): add a test named test_discount_coupons_not_supported_yet that is
    # unconditionally skipped with @pytest.mark.skip(reason="coupons not implemented, see backlog")
    # The body can just be `assert False`.
    @pytest.mark.skip(reason="coupons not implemented, see backlog")
    def test_discount_coupons_not_supported_yet(self):
        assert False

