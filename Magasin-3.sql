-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Apr 22, 2025 at 03:24 PM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `Magasin`
--

-- --------------------------------------------------------

--
-- Table structure for table `article`
--

CREATE TABLE `article` (
  `reference_article` varchar(40) NOT NULL,
  `nom_article` varchar(255) NOT NULL,
  `unite_prix` float NOT NULL,
  `quantite_stock` int(11) NOT NULL,
  `famille_article` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `article`
--

INSERT INTO `article` (`reference_article`, `nom_article`, `unite_prix`, `quantite_stock`, `famille_article`) VALUES
('ART001', 'Ballon de football', 12.99, 50, 'Ballon'),
('ART002', 'Haltère hexagonale 10kg', 24.99, 30, 'Haltère'),
('ART003', 'Raquette de tennis', 89.99, 15, 'Raquette'),
('ART004', 'Sac à dos 32L', 39.99, 20, 'Sac'),
('ART005', 'Short de running', 9.99, 80, 'Short'),
('ART006', 'Tente 2 seconds', 59.99, 12, 'Tente'),
('ART007', 'Vélo tout chemin', 299.99, 10, 'Vélo'),
('ART008', 'Ballon de basket', 14.99, 40, 'Ballon'),
('ART009', 'Haltère réglable 2x5kg', 49.99, 25, 'Haltère'),
('ART010', 'Raquette de badminton', 34.99, 35, 'Raquette'),
('ART011', 'Sac de sport 55L', 19.99, 60, 'Sac'),
('ART012', 'Short de football', 7.99, 100, 'Short'),
('ART013', 'Tente familiale', 129.99, 7, 'Tente'),
('ART014', 'Vélo VTT', 449.99, 4, 'Vélo');

-- --------------------------------------------------------

--
-- Table structure for table `commande`
--

CREATE TABLE `commande` (
  `reference_commande` int(11) NOT NULL,
  `montant_commande` double NOT NULL,
  `status_commande` enum('Terminer','En Cours') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `commande`
--

INSERT INTO `commande` (`reference_commande`, `montant_commande`, `status_commande`) VALUES
(1, 579.98, 'En Cours'),
(2, 0, 'En Cours');

-- --------------------------------------------------------

--
-- Table structure for table `contient`
--

CREATE TABLE `contient` (
  `reference_article` varchar(40) NOT NULL,
  `reference_commande` int(11) NOT NULL,
  `qte` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `contient`
--

INSERT INTO `contient` (`reference_article`, `reference_commande`, `qte`) VALUES
('ART013', 1, 1),
('ART014', 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `famille`
--

CREATE TABLE `famille` (
  `famille_article` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `famille`
--

INSERT INTO `famille` (`famille_article`) VALUES
('Ballon'),
('Haltère'),
('Raquette'),
('Sac'),
('Short'),
('Tente'),
('Vélo');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `article`
--
ALTER TABLE `article`
  ADD PRIMARY KEY (`reference_article`),
  ADD KEY `fk_famille_article` (`famille_article`);

--
-- Indexes for table `commande`
--
ALTER TABLE `commande`
  ADD PRIMARY KEY (`reference_commande`);

--
-- Indexes for table `contient`
--
ALTER TABLE `contient`
  ADD PRIMARY KEY (`reference_article`,`reference_commande`),
  ADD KEY `fk_ref_commande` (`reference_commande`);

--
-- Indexes for table `famille`
--
ALTER TABLE `famille`
  ADD PRIMARY KEY (`famille_article`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `article`
--
ALTER TABLE `article`
  ADD CONSTRAINT `fk_famille_article` FOREIGN KEY (`famille_article`) REFERENCES `famille` (`famille_article`);

--
-- Constraints for table `contient`
--
ALTER TABLE `contient`
  ADD CONSTRAINT `fk_ref_article` FOREIGN KEY (`reference_article`) REFERENCES `article` (`reference_article`),
  ADD CONSTRAINT `fk_ref_commande` FOREIGN KEY (`reference_commande`) REFERENCES `commande` (`reference_commande`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
