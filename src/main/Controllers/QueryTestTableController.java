import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.*;

import static java.time.temporal.ChronoUnit.DAYS;

public class QueryTestTableController extends QUERYS implements Initializable {

    public final ObservableSet<LocalDate> reservedDates =  FXCollections.observableSet(new TreeSet<>());

    public final ObservableSet<LocalDate> checkINDates =  FXCollections.observableSet(new TreeSet<>());
    public final ObservableSet<LocalDate> checkOUTDates =  FXCollections.observableSet(new TreeSet<>());
    public final ObservableSet<LocalDate> selectedDates =  FXCollections.observableSet(new TreeSet<>());

    @FXML public TableView<Hotels> amenitiesTable;
    @FXML public TableColumn <Hotels, String> amenities;
    @FXML
    public Button btn_Update;
    @FXML
    public Button btn_Create;
    @FXML
    public Button btn_Delete;
    @FXML
    public Button btn_Rooms;
    @FXML
    public TextField hotelInputTF;
    @FXML
    public DatePicker checkInDP;
    @FXML
    public DatePicker checkOutDP;

    @FXML
    private TableView<Hotels> hotelTable;
    @FXML private TableColumn<Hotels, String> Hotel, details, address,rating;
    @FXML private TableColumn<Hotels, Integer> Rooms, amenitiesTotal;
    @FXML private TableColumn<Hotels, Double> Price;

    //LocalDate minDate=java.util.Calendar.getInstance().getTime();
    //LocalDate minDate = LocalDate.of(2021, 11, 01);
    //private final ObservableSet<LocalDate> selectedDates;
    LocalDate minDate = LocalDate.now();
    LocalDate maxDate = LocalDate.of(2021, 12, 01);


    LocalDate currentDate = LocalDate.now();
    LocalDate resStartDate = LocalDate.of(2021, 11, 01);
    LocalDate resEndDate = LocalDate.of(2021, 12, 28);
    @FXML
    public Button resetBtn;
    @FXML
    public Button btn_search;
    @FXML
    public TextField cityTF;
    @FXML
    private DatePicker checkinDP;
    @FXML
    private DatePicker checkoutDP;

    @FXML
    private ObservableList<Hotels> list;
    private ObservableList<Hotels> amenitieslist;
    String hotelname = "The Magnolia All Suites";
    int hotel_ID;


    DynamicTable dt = new DynamicTable();
    LocalDate RcheckOut;
    LocalDate RcheckIn;



    public QueryTestTableController() throws ClassNotFoundException {
    }



    public void restrictDatePicker(DatePicker datePicker, LocalDate minDate, LocalDate maxDate) {
        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item.isBefore(minDate)) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        } else if (item.isAfter(maxDate)) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                };
            }
        };

        datePicker.setDayCellFactory(dayCellFactory);

        btn_Update.setOnMouseClicked(event -> {
            System.out.println("update pressed");
            Stage stage = new Stage();
            dt.setDbName2(hotelInputTF.getText().toString());//grab hotel from "Enter Hotel Name:"
            try {

                dt.start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }




   /** EventHandler<MouseEvent> mouseClickedEventHandler = clickEvent -> {
        if (clickEvent.getButton() == MouseButton.PRIMARY) {
            checkOutDP.show();
        }
        clickEvent.consume();
    };
    **/
   EventHandler<MouseEvent> mouseClickedEventHandler = (MouseEvent clickEvent) ->
   {
       if (clickEvent.getButton() == MouseButton.PRIMARY) {
           if (!this.selectedDates.contains(this.checkOutDP.getValue())) {
               this.selectedDates.add(checkOutDP.getValue());

               this.selectedDates.addAll(getRangeGaps((LocalDate) this.selectedDates.toArray()[0], (LocalDate) this.selectedDates.toArray()[this.selectedDates.size() - 1]));

           } else {
               this.selectedDates.remove(this.checkOutDP.getValue());
               this.selectedDates.removeAll(getTailEndDatesToRemove(this.selectedDates, this.checkOutDP.getValue()));

               this.checkOutDP.setValue(getClosestDateInTree(new TreeSet<>(this.selectedDates), this.checkOutDP.getValue()));

           }

       }
       this.checkOutDP.show();
       clickEvent.consume();
   };






    public void restrictDatePicker2(DatePicker datePicker, LocalDate resCheckIN, LocalDate resCheckOUT, ObservableSet<LocalDate> reservedDates) {


        /**
        datePicker.setDayCellFactory(dayCellFactory);

        btn_Update.setOnMouseClicked(event -> {
            System.out.println("update pressed");
            Stage stage = new Stage();
            dt.setDbName2(hotelInputTF.getText().toString());//grab hotel from "Enter Hotel Name:"
            try {

                dt.start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
         **/
        System.out.println(selectedDates);
        for(int i = 0; i < checkINDates.size(); i++){

            this.reservedDates.addAll(getRangeGaps((LocalDate) this.checkINDates.toArray()[i], (LocalDate) this.checkOUTDates.toArray()[i]));

        }



        System.out.println(reservedDates.size());
        datePicker.setDayCellFactory((DatePicker param) -> new DateCell()
        {



            @Override
            public void updateItem(LocalDate item, boolean empty)
            {

                super.updateItem(item, empty);

                //...

                if (item != null && !empty)
                {
                    //...
                    addEventHandler(MouseEvent.MOUSE_CLICKED, mouseClickedEventHandler);
                } else
                {
                    //...
                    removeEventHandler(MouseEvent.MOUSE_CLICKED, mouseClickedEventHandler);
                }
                if(item.isBefore(currentDate)){
                    setDisable(true);
                    setStyle("-fx-background-color: #FF4500;");
                }


                if (item.isBefore(resCheckOUT.plusDays(1) ) && item.isAfter(resCheckIN.minusDays(1))) {
                    setDisable(true);
                    setStyle("-fx-background-color: #FF4500;");
                }
                if (!reservedDates.isEmpty() && reservedDates.contains(item))
                {

                    setDisable(true);

                    setStyle("-fx-background-color: #FF4500;");

                }
                if (!reservedDates.isEmpty() && checkINDates.contains(item))
                {

                    setDisable(true);

                    setStyle("-fx-background-color: #FF4500;");

                }
                if (!reservedDates.isEmpty() && checkOUTDates.contains(item))
                {

                    setDisable(true);

                    setStyle("-fx-background-color: #FF4500;");

                }


                if (!selectedDates.isEmpty() && selectedDates.contains(item))
                {
                    if (Objects.equals(item, selectedDates.toArray()[0]) || Objects.equals(item, selectedDates.toArray()[selectedDates.size() - 1]))
                    {
                        System.out.println("yes");
                        setStyle("-fx-background-color: rgba(3, 169, 1, 0.7);");
                    } else
                    {
                        System.out.println("yes -1");
                        setStyle("-fx-background-color: rgba(3, 169, 244, 0.7);");
                    }
                } else
                {
                    setStyle(null);
                }

            }
        });
    }



    private static Set<LocalDate> getTailEndDatesToRemove(Set<LocalDate> dates, LocalDate date)
    {

        TreeSet<LocalDate> tempTree = new TreeSet<>(dates);

        tempTree.add(date);

        int higher = tempTree.tailSet(date).size();
        int lower = tempTree.headSet(date).size();

        if (lower <= higher)
        {
            return tempTree.headSet(date);
        } else if (lower > higher)
        {
            return tempTree.tailSet(date);
        } else
        {
            return new TreeSet<>();
        }

    }
    private static Set<LocalDate> getRangeGaps(LocalDate min, LocalDate max)
    {
        Set<LocalDate> rangeGaps = new LinkedHashSet<>();

        if (min == null || max == null)
        {
            return rangeGaps;
        }

        LocalDate lastDate = min.plusDays(1);
        while (lastDate.isAfter(min) && lastDate.isBefore(max))
        {
            rangeGaps.add(lastDate);
            lastDate = lastDate.plusDays(1);

        }
        return rangeGaps;
    }

    private static LocalDate getClosestDateInTree(TreeSet<LocalDate> dates, LocalDate date)
    {
        Long lower = null;
        Long higher = null;

        if (dates.isEmpty())
        {
            return null;
        }

        if (dates.size() == 1)
        {
            return dates.first();
        }

        if (dates.lower(date) != null)
        {
            lower = Math.abs(DAYS.between(date, dates.lower(date)));
        }
        if (dates.higher(date) != null)
        {
            higher = Math.abs(DAYS.between(date, dates.higher(date)));
        }

        if (lower == null)
        {
            return dates.higher(date);
        } else if (higher == null)
        {
            return dates.lower(date);
        } else if (lower <= higher)
        {
            return dates.lower(date);
        } else if (lower > higher)
        {
            return dates.higher(date);
        } else
        {
            return null;
        }
    }

    public void populateTableView() throws SQLException, ClassNotFoundException {
        System.out.println("populateTableView Called");
        //PreparedStatement ps = con.prepareStatement("call hotel.getListAvailHotels(?)");
        //CallableStatement callableStatement = con.prepareCall("{call hotel.getListAvailHotels}");
        ResultSet rs = dbExecuteCallQuery("call hotel.getListAvailHotels");

        ResultSet rs2 = getAmenitiesByHotelName(hotelname); //for Amenities TableView


        if (checkinDP.getValue() != null) {
            System.out.println("DATE PICKERS HAVE VALUES!");
            // check_in_date = checkinDP.getValue();
            // check_out_date = checkinDP.getValue();
            // ps.setDate(1, Date.valueOf(String.valueOf(check_in_date)));
            // ps.setDate(2, Date.valueOf(String.valueOf(check_out_date)));

        } else if (checkinDP.getValue() == null) {

            System.out.println("DATEPICKERS NULL USE DEFUALT INSTEAD");

            // long millis = System.currentTimeMillis();
            // java.sql.Date date = new java.sql.Date(millis);
            //ps.setDate(1, date);
            // ps.setDate(2, date);
        }



        //loop through the resultSet , extract data and append it to our list
        while (rs.next() ) {

            //Create a hotels Object , add data to it and finally append it to list
            Hotels hotel = new Hotels();
            hotel.setHotelname(rs.getString("hotel_name"));
            hotel.setRooms(rs.getInt("hotel_availrms"));
            hotel.setAmenities(rs.getInt("hotel_numofamend"));
            hotel.setPrice(rs.getDouble("room_rate"));
            hotel.setRating(rs.getInt("hotel_rating"));
            hotel.setHoteladdr(rs.getString("hotel_address"));
            hotel.setHoteldesc(rs.getString("hotel_desc"));
            hotel.setHotelId(rs.getInt("hotel_id"));

            list.add(hotel);
        }

        while(rs2.next()){
            Hotels hotel = new Hotels();
            System.out.println(rs2.getString("Amenities_desc"));

            hotel.setAmenitiesdesc(rs2.getString("Amenities_desc"));

            amenitieslist.add(hotel);

        }

        amenities.setCellValueFactory(new PropertyValueFactory<>("amenitiesdesc"));
        Hotel.setCellValueFactory(new PropertyValueFactory<>("hotelname"));
        Rooms.setCellValueFactory(new PropertyValueFactory<>("rooms"));
        Price.setCellValueFactory(new PropertyValueFactory<>("price"));
        amenitiesTotal.setCellValueFactory(new PropertyValueFactory<>("amenities"));
        details.setCellValueFactory(new PropertyValueFactory<>("hoteldesc"));
        address.setCellValueFactory(new PropertyValueFactory<>("hoteladdr"));
        rating.setCellValueFactory(new PropertyValueFactory<>("rating"));

        // Hotel.setCellValueFactory(c-> new SimpleStringProperty(hotel.getHotelname()));

        //set data tp tableview
        hotelTable.setItems(list);
        amenitiesTable.setItems(amenitieslist);



/**

 firstNameCol.setCellValueFactory(new Callback<CellDataFeatures<Data, String>, ObservableValue<String>>() {
 public ObservableValue<String> call(CellDataFeatures<Data, String> data) {
 // data.getValue() returns the Data instance for a particular TableView row
 return data.getValue().getUser().firstNameProperty();
 }
 });
 }


        hotelTable.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE
        );
        amenitiesTable.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE
        );

        btn_Update.setOnMouseClicked(event -> {
            System.out.println("update pressed");
            System.out.println(hotelTable.getSelectionModel().getSelectedCells().toString());
            hotelTable.setEditable(true);
            hotelTable.getSelectionModel().cellSelectionEnabledProperty().set(true);
        });
**/
//ON DOUBLE CLICK ANY ROW WILL GET THAT HOTEL NAME AND STORE IT INSIDE GLOBAL hotelname
        hotelTable.setRowFactory( tv -> {
            TableRow<Hotels> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    reservedDates.clear();
                    checkOUTDates.clear();
                    checkINDates.clear();
                    Hotels rowData = row.getItem();
                    hotelname = rowData.getHotelname();
                    hotel_ID = rowData.getHotelId();
                    System.out.println(rowData.getHotelId());
                    System.out.println(rowData.getHotelname());
                    try {
                        ResultSet rslist = getOverLappingDates(resStartDate,resEndDate,"Queen",hotel_ID );
                        while(rslist.next()){
                            RcheckIn = rslist.getDate("res_check_in").toLocalDate();

                            RcheckOut = rslist.getDate("res_check_out").toLocalDate();


                            checkINDates.add(RcheckIn);
                            checkOUTDates.add(RcheckOut);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                    System.out.println(checkINDates);
                    System.out.println(checkOUTDates);

                    restrictDatePicker2(checkOutDP,RcheckIn, RcheckOut, reservedDates);
                    //System.out.println(reservedDates);



                    list.clear();
                    amenitieslist.clear();
                    try {
                        populateTableView();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }

            });
            return row ;
        });



    }









    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //MultiDatePicker multiDatePicker = new MultiDatePicker().withRangeSelectionMode();

        //DatePicker rangePicker = multiDatePicker.getDatePicker();
        restrictDatePicker(checkInDP,minDate,maxDate);
       //restrictDatePicker2(checkOutDP, resStartDate,resEndDate, reservedDates);
        list = FXCollections.observableArrayList();
        amenitieslist = FXCollections.observableArrayList();
       // MultiDatePicker multiDatePicker = new MultiDatePicker().withRangeSelectionMode();
       // checkOutDP = multiDatePicker.getDatePicker();

        try {

            populateTableView();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }






    }
}
