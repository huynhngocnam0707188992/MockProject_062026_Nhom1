import { Button } from "@/components/ui/button";
import Text from "../ui/Text";
import {
  Table,
  TableBody,
  TableCaption,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "../ui/table";
import Flag from "../ui/flag";
import { Link } from "react-router";
type Resident = {
  id: number;
  name: string;
  locTier: string;
  status: string;
  lastReview: string;
  nextReview: string;
  assinged: string;
};

const listResident: Resident[] = [
  {
    id: 1,
    name: "Susan Wright",
    locTier: "Tier 1",
    status: "Needs Update",
    lastReview: "2026-03-30",
    nextReview: "overdue",
    assinged: "Anna lee",
  },
  {
    id: 2,
    name: "Susan Wright2",
    locTier: "Tier 2",
    status: "Active ",
    lastReview: "2026-04-04",
    nextReview: "2026-07-07",
    assinged: "Anna lee",
  },
];

export default function CarePlanTable() {
  return (
    <div className="mt-[16px]">
      <Table className=" border-2 border-gray-300 rounded-lg p-4">
        {/* <TableCaption>A list of your recent invoices.</TableCaption> */}
        <TableHeader className="bg-gray-300 ">
          <TableRow className="font-bold font text-lg ">
            <TableHead className="w-[180ppx] text-left text-gray-600">
              {/* Resident */}
              <Text>Resident</Text>
            </TableHead>
            <TableHead className="text-left  text-gray-600">
              <Text>LOC Tier</Text>
            </TableHead>
            <TableHead className="text-left text-gray-600">
              <Text>Status</Text>
            </TableHead>
            <TableHead className="text-left text-gray-600">
              <Text>Last Review</Text>
            </TableHead>
            <TableHead className="text-left text-gray-600">
              <Text>Next Review</Text>
            </TableHead>
            <TableHead className="text-left">
              <Text>Assigned</Text>
            </TableHead>
            <TableHead className="text-left"></TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {listResident.map((resident) => (
            <TableRow key={resident.id}>
              <TableCell className="font-medium">
                <Text className="text-black">{resident.name}</Text>
              </TableCell>

              <TableCell className="text-left">
                <Text>{resident.locTier}</Text>
              </TableCell>

              <TableCell className="text-left">
                <Flag
                  title={resident.status}
                  className={`rounded-full ${
                    resident.status === "Needs Update"
                      ? "bg-red-300 text-red-700 border-red-400"
                      : "bg-green-300 text-green-700 border-green-400"
                  }`}
                ></Flag>
              </TableCell>

              <TableCell className="text-left">
                <Text>{resident.lastReview}</Text>
              </TableCell>

              <TableCell className="text-left ">
                <Text>{resident.nextReview}</Text>
              </TableCell>

              <TableCell className="text-left">
                <Text>{resident.assinged}</Text>
              </TableCell>

              <TableCell className="text-left cursor-pointer">
                <TableCell className="text-left">
                  <Link to={`/admin/care-plans/${resident.id}`}>
                <Text className="text-blue-700 font-bold ">View</Text>
                  </Link>
                </TableCell>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </div>
  );
}
