-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Aug 14, 2022 at 04:37 PM
-- Server version: 10.4.22-MariaDB
-- PHP Version: 8.1.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--

--
-- Database: `erpdb`
--
CREATE DATABASE IF NOT EXISTS `erpdb` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `erpdb`;

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

CREATE TABLE `orders` (
  `id` int(11) NOT NULL,
  `productId` int(11) NOT NULL,
  `qty` int(11) NOT NULL,
  `customerName` varchar(250) NOT NULL,
  `customerContact` varchar(250) NOT NULL,
  `leadTime` varchar(100) NOT NULL,
  `datePlaced` varchar(250) NOT NULL,
  `status` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `orders`
--

INSERT INTO `orders` (`id`, `productId`, `qty`, `customerName`, `customerContact`, `leadTime`, `datePlaced`, `status`) VALUES
(21, 19, 15, 'shadeeer', '6565465', '02', 'Tue, Jul 05 2022', 'completed'),
(22, 20, 5, 'customer1', '8468468', '2', 'Tue, Jul 05 2022', 'completed'),
(23, 20, 5, 'customer2', 'hbhjbj', '2', 'Wed, Jul 06 2022', 'completed'),
(24, 20, 50, 'customer3', '56767576576', '10', 'Wed, Jul 13 2022', 'completed'),
(25, 19, 5000, 'customer4', '232312313', '100', 'Wed, Jul 13 2022', 'completed'),
(26, 20, 1200, 'customer5', '2312341321', '150', 'Wed, Jul 13 2022', 'completed'),
(27, 20, 20, 'customer6', '7675665', '20', 'Wed, Jul 13 2022', 'completed'),
(28, 19, 50, 'customer7', 'hgjhbjh', '20', 'Wed, Jul 13 2022', 'completed'),
(29, 19, 5, 'customer8', 'lkjknj', '7', 'Wed, Jul 13 2022', 'completed'),
(30, 19, 3, 'customer9', 'adawdaw', '6', 'Wed, Jul 13 2022', 'completed'),
(31, 19, 5, 'customer10', 'adada', '5', 'Wed, Jul 13 2022', 'completed');

-- --------------------------------------------------------

--
-- Table structure for table `parts`
--

CREATE TABLE `parts` (
  `id` int(11) NOT NULL,
  `partName` varchar(250) NOT NULL,
  `belongingProductId` int(11) NOT NULL,
  `unitsPerProduct` int(11) NOT NULL,
  `stock` int(11) NOT NULL,
  `unitPrice` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `parts`
--

INSERT INTO `parts` (`id`, `partName`, `belongingProductId`, `unitsPerProduct`, `stock`, `unitPrice`) VALUES
(19, 'fan blades', 19, 1, 50, '350'),
(20, 'blade housing', 19, 1, 50, '275'),
(21, 'motor housing', 19, 1, 50, '260'),
(22, 'stand', 19, 1, 50, '270'),
(23, 'screw m4', 19, 18, 500, '8'),
(24, 'screw m3', 19, 6, 250, '6'),
(25, 'motor', 19, 1, 100, '1200'),
(26, 'headrest', 20, 1, 200, '640'),
(27, 'seat pad', 20, 1, 100, '1100'),
(28, 'gas cylinder', 20, 1, 50, '1200'),
(29, 'arms ', 20, 2, 100, '470'),
(30, 'wheels', 20, 6, 100, '120'),
(31, 'screw 008', 20, 8, 200, '7'),
(32, 'screw 002', 20, 5, 150, '3');

-- --------------------------------------------------------

--
-- Table structure for table `products`
--

CREATE TABLE `products` (
  `id` int(11) NOT NULL,
  `productName` varchar(250) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `products`
--

INSERT INTO `products` (`id`, `productName`) VALUES
(19, 'table fan'),
(20, 'office chair');

-- --------------------------------------------------------

--
-- Table structure for table `sales`
--

CREATE TABLE `sales` (
  `id` int(11) NOT NULL,
  `orderId` int(11) NOT NULL,
  `datePerformed` varchar(250) NOT NULL,
  `earning` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `sales`
--

INSERT INTO `sales` (`id`, `orderId`, `datePerformed`, `earning`) VALUES
(7, 21, 'Tue, Jul 05 2022', '38025'),
(8, 21, 'Tue, Jul 05 2022', '38025'),
(9, 22, 'Tue, Jul 05 2022', '14700'),
(10, 23, 'Wed, Jul 06 2022', '35855'),
(11, 24, 'Wed, Jul 13 2022', '233550'),
(12, 25, 'Wed, Jul 13 2022', '12675000'),
(13, 26, 'Wed, Jul 13 2022', '5605200'),
(14, 27, 'Wed, Jul 13 2022', '93420'),
(15, 28, 'Wed, Jul 13 2022', '126750'),
(16, 29, 'Wed, Jul 13 2022', '12675'),
(17, 30, 'Wed, Jul 13 2022', '7605'),
(18, 31, 'Wed, Jul 13 2022', '12675');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `userName` varchar(250) NOT NULL,
  `passwd` varchar(250) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `userName`, `passwd`) VALUES
(1, 'admin', 'admin1234');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `parts`
--
ALTER TABLE `parts`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `sales`
--
ALTER TABLE `sales`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `orders`
--
ALTER TABLE `orders`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=32;

--
-- AUTO_INCREMENT for table `parts`
--
ALTER TABLE `parts`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=34;

--
-- AUTO_INCREMENT for table `products`
--
ALTER TABLE `products`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT for table `sales`
--
ALTER TABLE `sales`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
