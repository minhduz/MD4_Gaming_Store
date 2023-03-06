package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ra.model.entity.*;
import ra.model.service.CartItemService;
import ra.model.service.OrderDetailService;
import ra.model.service.OrdersService;
import ra.model.service.UserService;
import ra.payload.response.GameDTO;
import ra.payload.response.OrderDTO;
import ra.payload.response.OrderDetailDTO;
import ra.security.CustomUserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("api/v1/orders")
public class OrdersController {
    @Autowired
    CartItemService<CartItem,Integer> cartItemService;
    @Autowired
    OrdersService<Orders,Integer> ordersService;
    @Autowired
    OrderDetailService<OrderDetail,Integer> orderDetailService;
    @Autowired
    UserService<User,Integer> userService;

    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    @GetMapping("getAll")
    public List<Orders> getAll(){
        return ordersService.findAll();
    }

    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    @GetMapping("getAll/{ordersID}")
    public Orders getByID(@PathVariable("ordersID")int ordersID){
        return ordersService.findByID(ordersID);
    }

    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    @GetMapping("updateStatus/{ordersID}")
    public Orders updateStatus(@PathVariable("ordersID")int ordersID,@RequestBody Orders orders){
        Orders orderUpdate = ordersService.findByID(ordersID);
        orderUpdate.setOrderStatus(orders.isOrderStatus());
        ordersService.saveOrUpdate(orderUpdate);
        return orderUpdate;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("create")
    public ResponseEntity<?> createOrder(){

        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByID(userDetails.getUserID());
        if(user.getListCartItem().size()!=0){
            Orders orders = new Orders();
            orders.setOrderDate(LocalDate.now());
            orders.setOrderStatus(false);
            orders.setOrderTotalAmount(totalAmount(user.getListCartItem()));
            orders.setUser(user);
            ordersService.saveOrUpdate(orders);

            for (CartItem cartItem:user.getListCartItem()) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrders(orders);
                orderDetail.setOrderAmount(cartItem.getCartPrice());
                orderDetail.setOrderPrice(cartItem.getGame().getGamePrice()*(100-cartItem.getGame().getGameDiscount())/100);
                orderDetail.setOrderQuantity(cartItem.getCartQuantity());
                orderDetail.setGame(cartItem.getGame());
                orderDetailService.saveOrUpdate(orderDetail);
                cartItemService.deleteCart123(cartItem.getCartItemID());
            }
            return ResponseEntity.ok("Add into order successfully");
        }else {
            return ResponseEntity.ok("Add into order failed because don't have anything in cart");
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("getOrdersByUser")
    public List<OrderDTO> listOrders(){
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Orders> listOrders = ordersService.findAllByUser_UserID(userDetails.getUserID());
        List<OrderDTO> listOrderDTO = new ArrayList<>();
        for (Orders orders:listOrders) {
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setOrderID(orders.getOrderID());
            orderDTO.setOrderDate(orders.getOrderDate());
            orderDTO.setOrderTotalAmount(orders.getOrderTotalAmount());
            orderDTO.setOrderStatus(orders.isOrderStatus());
            List<OrderDetailDTO> listODDTO = new ArrayList<>();
            for (OrderDetail od: orders.getListOrderDetail()) {
                OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
                orderDetailDTO.setOrderDetailID(od.getOrderDetailID());
                orderDetailDTO.setOrderPrice(od.getOrderPrice());
                orderDetailDTO.setOrderQuantity(od.getOrderQuantity());
                orderDetailDTO.setOrderAmount(od.getOrderAmount());
                Game game = od.getGame();
                GameDTO gameDTO = new GameDTO();
                gameDTO.setGameID(game.getGameID());
                gameDTO.setGameName(game.getGameName());
                gameDTO.setGameMainImage(game.getGameMainImage());
                gameDTO.setGamePrice(game.getGamePrice());
                gameDTO.setGameStatus(game.isGameStatus());
                orderDetailDTO.setGameDTO(gameDTO);
                listODDTO.add(orderDetailDTO);
            }
            orderDTO.setListOrderDetailDTO(listODDTO);
            listOrderDTO.add(orderDTO);
        }
        return listOrderDTO;
    }



    public float totalAmount(List<CartItem> cartItems){
        float totalAmount = 0;
        for (CartItem cartItem:cartItems){
            totalAmount += cartItem.getCartQuantity() * (cartItem.getGame().getGamePrice() * (100 - cartItem.getGame().getGameDiscount())/100);
        }
        return totalAmount;
    }
}
